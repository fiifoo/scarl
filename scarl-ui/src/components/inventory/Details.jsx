import { fromJS, List, Map, Set } from 'immutable'
import React from 'react'
import { LauncherSlot } from '../../game/equipment'
import { stats as equipmentStats } from '../../game/creature'
import { groups as equipmentGroups, slots as equipmentSlots } from '../../game/equipment'
import { addStats } from '../../game/utils'

const Diff = ({amount}) => (
    <span className="text-muted">(<span className={amount > 0 ? 'text-success' : 'text-danger'}>
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
                <td>{diff !== 0 ? <Diff amount={diff} /> : null}</td>
            </tr>
        )
    }

    const filterStat = (_, path) => stats.getIn(path) > 0
    const filterCompareStat = (_, path) => stats.getIn(path) === 0 && compare.getIn(path) > 0

    return (
        <tbody>
            <tr>
                <th></th>
                <th colSpan="2">{label}</th>
            </tr>
            {equipmentStats.filter(filterStat).map(renderStat).toArray()}
            {compare
                ? equipmentStats.filter(filterCompareStat).map(renderStat).toArray()
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

const Details = ({equipments, inventory, item, kinds}) => {
    const kind = kinds.items.get(item.kind)

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
            <React.Fragment key={key}>
                {content}
            </React.Fragment>
        )
    }

    const stats = Map(equipmentGroups).filter(group => item[group.prop]).map(renderEquipment).toArray()

    const missile = item.launcher && item.launcher.stats.launcher.missile ? (
        <MissileStats
            equipments={equipments}
            inventory={inventory}
            item={item}
            kinds={kinds} />
    ) : null

    return (
        <div>
            <h4>{kind.name}</h4>
            <table>
                {stats}
                {missile}
            </table>
        </div>
    )
}

export default Details
