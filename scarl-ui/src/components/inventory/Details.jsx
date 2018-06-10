import { fromJS, Map } from 'immutable'
import React from 'react'
import { groups as equipmentGroups, stats as equipmentStats } from '../../game/equipment'

const Stats = ({stats, label}) => {
    stats = fromJS(stats)

    const renderStat = (label, path) => (
        <tr key={path.join('.')}>
            <th className="text-right">{label}</th>
            <td>{stats.getIn(path)}</td>
        </tr>
    )

    return (
        <tbody>
            <tr>
                <th></th>
                <th>{label}</th>
            </tr>
            {equipmentStats.filter((_, path) => stats.getIn(path) > 0).map(renderStat).toArray()}
        </tbody>
    )
}

const Details = ({item, kinds}) => {
    const kind = kinds.items.get(item.kind)

    const renderEquipment = (group, key) => {
        const equipment = item[group.prop]

        return (
            <Stats
                key={key}
                stats={equipment.stats}
                label={group.label} />
        )
    }

    const equipments = Map(equipmentGroups).filter(group => item[group.prop]).map(renderEquipment).toArray()

    const missile = item.launcher && item.launcher.stats.launcher.missile ? (
        <Stats label="Missile" stats={kinds.creatures.get(item.launcher.stats.launcher.missile).stats} />
    ) : null

    return (
        <div>
            <h4>{kind.name}</h4>
            <table>
                {equipments}
                {missile}
            </table>
        </div>
    )
}

export default Details
