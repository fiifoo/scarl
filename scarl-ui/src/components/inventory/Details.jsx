import { fromJS, List, Map, Set } from 'immutable'
import React, { Fragment } from 'react'
import { LauncherSlot } from '../../game/equipment'
import { stats as creatureStats, negativeStats } from '../../game/creature'
import { groups as equipmentGroups, slots as equipmentSlots } from '../../game/equipment'
import { resourceStats } from '../../game/power'
import { addStats } from '../../game/utils'

const isPositive = (path, amount) => {
    const positiveStat = ! negativeStats.contains(path)

    return amount >= 0 ? positiveStat : ! positiveStat
}

const Diff = ({path, amount}) => (
    <span className="text-muted">(<span className={isPositive(path, amount) ? 'text-success' : 'text-danger'}>
        {amount > 0 ? '+' : null}{amount}
    </span>)</span>
)

const SimpleStats = ({label, values, properties}) => {
    values = fromJS(values)

    const renderStat = (label, path) => {
        const value = values.getIn(path)

        return (
            <tr key={path.join('.')}>
                <th className="text-right">{label}</th>
                <td>{value}</td>
                <td></td>
            </tr>
        )
    }

    const filterStat = (_, path) => values.getIn(path) !== 0

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

const Stats = ({label, compare, stats}) => {
    compare = compare ? fromJS(compare) : null
    stats = fromJS(stats)

    const renderStat = (label, path) => {
        const value = stats.getIn(path)
        const compareValue = compare ? compare.getIn(path) : 0
        const diff = value - compareValue

        return (
            <tr key={path.join('.')} className={value === 0 ? 'text-muted' : null}>
                <th className="text-right">{label}</th>
                <td>{value}</td>
                <td>{diff !== 0 ? <Diff path={path} amount={diff} /> : null}</td>
            </tr>
        )
    }

    const filterStat = (_, path) => stats.getIn(path) !== 0
    const filterCompareStat = (_, path) => stats.getIn(path) === 0 && compare.getIn(path) !== 0

    return (
        <tbody>
            {label && (
                <tr>
                    <th></th>
                    <th colSpan="2">{label}</th>
                </tr>
            )}
            {creatureStats.filter(filterStat).map(renderStat).toArray()}
            {compare
                ? creatureStats.filter(filterCompareStat).map(renderStat).toArray()
                : null
            }
        </tbody>
    )
}

const MissileStats = ({equipments, inventory, item, kinds}) => {
    const stats = kinds.creatures.get(item.launcher.stats.launcher.missile).stats

    const compareItemId = equipments.get(LauncherSlot)
    const compareItem = compareItemId ? inventory.get(compareItemId) : null
    const compare = compareItem ? kinds.creatures.get(compareItem.launcher.stats.launcher.missile).stats : null

    return <Stats label="Missile" stats={stats} compare={compare} />
}

const ExplosiveStats = ({explosive}) => {
    const properties = creatureStats.filter((_, path) => path.first() === 'explosive')

    return <SimpleStats values={{explosive}} properties={properties} />
}

const KindStats = ({id, kinds}) => {
    switch (id.get('type')) {
        case 'CreatureKindId': {
            const creature = kinds.creatures.get(id.get('value'))

            return <SimpleStats values={creature.stats} properties={creatureStats} />
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
                    carry ? addStats(carry, stats) : stats
                ), null)

                return {
                    compare: carry.compare ? addStats(carry.compare, compareStats) : compareStats,
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
            <Stats
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

const ActionsDropdown = ({selected, actions, setAction}) =>  (
    <div className="actions-dropdown">
        <div
            className="toggle"
            onClick={() => setAction(selected === null ? 0 : null)}>
            ▼
        </div>
        <div className={selected === null ? 'menu closed' : 'menu'}>
            {actions.map((action, key) => (
                <div
                    key={key}
                    className={key === selected ? 'active' : null}
                    onClick={action.execute}>
                    {action.label}
                </div>
            ))}
        </div>
    </div>
)

const Details = ({action, actions, equipments, inventory, item, kinds, setAction}) => (
    <div>
        <h4>{kinds.items.get(item.kind).name}</h4>

        {actions.isEmpty() ? null : (
            <ActionsDropdown
                selected={action}
                actions={actions}
                setAction={setAction} />
        )}

        <table className="scarl-table">

            <EquipmentStats
                equipments={equipments}
                inventory={inventory}
                item={item} />

            {item.launcher && item.launcher.stats.launcher.missile ? (
                <MissileStats
                    equipments={equipments}
                    inventory={inventory}
                    item={item}
                    kinds={kinds} />
            ) : null}

            {item.usable ? (
                <UsableStats item={item} kinds={kinds} />
            ) : null}

        </table>
    </div>
)

export default Details
