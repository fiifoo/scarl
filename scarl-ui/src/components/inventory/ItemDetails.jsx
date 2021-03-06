import { fromJS, List, Map, Set } from 'immutable'
import React, { Fragment } from 'react'
import { LauncherSlot } from '../../game/equipment'
import { groups as equipmentGroups, slots as equipmentSlots } from '../../game/equipment'
import { resourceStats } from '../../game/power'
import Stats from '../../game/Stats'

const Diff = ({diff}) => {
    const renderSingle = (diff, key) => (
        <span key={key} className={diff.positive ? 'text-success' : 'text-danger'}>
            {diff.displayValue}
        </span>
    )

    return (
        <span className="text-muted">({diff.map(renderSingle)})</span>
    )
}

const SimpleStats = ({label, values, properties}) => {
    values = fromJS(values)

    const renderStat = (label, path) => {
        const value = values.getIn(path)

        return (
            <tr key={path.join('.')}>
                <th className="text-right">{label}</th>
                <td className="description">{Stats.getDisplayValue(path)(value)}</td>
                <td></td>
            </tr>
        )
    }

    const filterStat = (_, stat) => ! Stats.isEmpty(stat)(values.getIn(stat))

    return (
        <tbody>
            {label && (
                <tr>
                    <th></th>
                    <th colSpan="2">{label}</th>
                </tr>
            )}
            {properties.filter(filterStat).map(renderStat).toArray()}
        </tbody>
    )
}

const ItemStats = ({label, compare, stats}) => {
    compare = compare ? fromJS(compare) : null
    stats = fromJS(stats)

    const renderStat = (label, path) => {
        const value = stats.getIn(path)
        const compareValue = compare ? compare.getIn(path) : null
        const empty = Stats.isEmpty(path)(value)
        const diff = Stats.diff(path)(value, compareValue)
        const verbose = Stats.isVerbose(path)

        return (
            <tr key={path.join('.')} className={empty ? 'text-muted' : null}>
                <th className="text-right">{label}</th>
                <td className="description">{Stats.getDisplayValue(path)(value)}</td>
                <td>{verbose || diff.isEmpty() ? null : <Diff diff={diff} />}</td>
            </tr>
        )
    }

    const filterStat = (_, stat) => ! Stats.isEmpty(stat)(stats.getIn(stat))
    const filterCompareStat = (_, stat) => Stats.isEmpty(stat)(stats.getIn(stat)) && ! Stats.isEmpty(stat)(compare.getIn(stat))

    return (
        <tbody>
            {label && (
                <tr>
                    <th></th>
                    <th colSpan="2">{label}</th>
                </tr>
            )}
            {Stats.stats.filter(filterStat).map(renderStat).toArray()}
            {compare
                ? Stats.stats.filter(filterCompareStat).map(renderStat).toArray()
                : null
            }
        </tbody>
    )
}

const MissileStats = ({equipments, inventory, item, kinds}) => {
    const compareItemId = equipments.get(LauncherSlot)
    const compareItem = compareItemId ? inventory.get(compareItemId) : null

    return item.launcher.stats.launcher.missiles.map((missile, index) => {
        const kind = kinds.creatures.get(missile)
        const stats = kind.stats
        const compareMissile = compareItem && compareItem.launcher.stats.launcher.missiles[index]
        const compare = compareMissile ? kinds.creatures.get(compareMissile).stats : null

        return <ItemStats key={index} label={kind.name} stats={stats} compare={compare} />
    })
}

const ExplosiveStats = ({explosive}) => {
    const properties = Stats.stats.filter((_, path) => path.first() === 'explosive')

    return <SimpleStats values={{explosive}} properties={properties} />
}

const KindStats = ({id, kinds}) => {
    switch (id.get('type')) {
        case 'CreatureKindId': {
            const creature = kinds.creatures.get(id.get('value'))

            return <SimpleStats values={creature.stats} properties={Stats.stats} />
        }
        case 'WidgetKindId': {
            const itemId = kinds.widgets.get(id.get('value')).data.item
            const item = kinds.items.get(itemId)

            return item.explosive ? <ExplosiveStats explosive={item.explosive} /> : null
        }
        default: {
            return null
        }
    }
}

const UsableStats = ({item, kinds}) => {
    const type = item.usable.type
    const power = fromJS(item.usable.data)

    const target = (() => {
        switch (type) {
            case 'CreateEntityPower': {
                return power.get('kind')
            }
            case 'TransformPower': {
                return power.get('transformTo')
            }
            default: {
                return null
            }
        }
    })()

    const renderResource = (label, path) => {
        const value = power.getIn(path)

        return (
            <tr key={path.join('.')}>
                <th className="text-right">{label}</th>
                <td className={value > 0 ? 'text-success' : 'text-danger'}>
                    {value > 0 ? '+' : null}{value}
                </td>
                <td></td>
            </tr>
        )
    }

    const filterResource = (_, path) => power.getIn(path) !== 0

    return (
        <Fragment>
            {power.has('resources') && (
                <tbody>
                    {resourceStats.filter(filterResource).map(renderResource).toArray()}
                </tbody>
            )}
            {target && <KindStats id={target} kinds={kinds} />}
        </Fragment>
    )
}

const EquipmentStats = ({equipments, inventory, item}) => {
    const renderStats = (group, stats) => (slots, index = 0) => {
        const compare = slots.reduce((carry, slot) => {
            const compareItemId = equipments.get(slot)

            if (compareItemId && !carry.items.contains(compareItemId)) {
                const compareItem = inventory.get(compareItemId)
                const compareGroups = Map(equipmentGroups).filter(x => compareItem[x.prop]).filter(x => x.slots(compareItem).contains(slot))
                const compareStats = compareGroups.map(group => group.getSlotStats(compareItem, slot)).reduce((carry, stats) => (
                    carry ? Stats.add(carry, stats) : stats
                ), null)

                return {
                    compare: carry.compare ? Stats.add(carry.compare, compareStats) : compareStats,
                    items: carry.items.add(compareItemId)
                }
            } else {
                return carry
            }
        }, {
            compare: null,
            items: Set(),
        }).compare

        const label = slots.map(x => equipmentSlots[x].label).join(', ')

        return (
            <ItemStats
                key={index}
                compare={compare}
                label={label}
                stats={stats} />
        )
    }

    const renderEquipment = (group, key) => {
        const slots = group.slots(item)
        const fillAll = group.fillAll(item)

        const content = fillAll ? (
            renderStats(group, item[group.prop].stats)(slots)
        ) : (
            slots.map((slot, index) => {
                const stats = group.getSlotStats(item, slot)

                return renderStats(group, stats)(List([slot]), index)
            })
        )

        return (
            <Fragment key={key}>
                {content}
            </Fragment>
        )
    }

    return (
        <Fragment>
            {Map(equipmentGroups).filter(group => item[group.prop]).map(renderEquipment).toArray()}
        </Fragment>
    )
}

const Description = ({item}) => {
    return ! item.description ? null : (
        <tbody>
            <tr>
                <td></td>
                <td colSpan="2" className="description text-muted">{item.description}</td>
            </tr>
        </tbody>
    )
}

const ItemDetails = ({equipments, inventory, item, kinds}) => (
    <Fragment>
        <Description item={item} />

        <EquipmentStats
            equipments={equipments}
            inventory={inventory}
            item={item} />

        {item.launcher && item.launcher.stats.launcher.missiles.length > 0 ? (
            <MissileStats
                equipments={equipments}
                inventory={inventory}
                item={item}
                kinds={kinds} />
        ) : null}

        {item.usable ? (
            <UsableStats item={item} kinds={kinds} />
        ) : null}
    </Fragment>
)

export default ItemDetails
