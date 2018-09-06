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
            <tr>
                <th></th>
                <th colSpan="2">{label}</th>
            </tr>
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

const UsableStats = ({item}) => {
    const power = fromJS(item.usable.data)

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
        <tbody>
            {resourceStats.filter(filterResource).map(renderResource).toArray()}
        </tbody>
    )
}

const EquipmentStats = ({equipments, inventory, item}) => {
    const renderStats = (group, stats) => (slots, index = 0) => {
        const compare = slots.reduce((carry, slot) => {
            const compareItemId = equipments.get(slot)

            if (compareItemId && !carry.items.contains(compareItemId)) {
                const compareItem = inventory.get(compareItemId)
                const compareGroups = Map(equipmentGroups).filter(x => compareItem[x.prop]).filter(x => x.slots(compareItem).contains(slot))
                const compareStats = compareGroups.map(x => compareItem[x.prop].stats).reduce((carry, stats) => (
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
        const equipment = item[group.prop]
        const slots = group.slots(item)
        const fillAll = group.fillAll(item)

        const content = fillAll ? (
            renderStats(group, equipment.stats)(slots)
        ) : (
            slots.map(x => List([x])).map(renderStats(group, equipment.stats))
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
                <UsableStats item={item} />
            ) : null}

        </table>
    </div>
)

export default Details
