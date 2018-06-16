import React from 'react'
import { fromJS } from 'immutable'
import { stats as creatureStats } from '../../game/creature'
import { addStats } from '../../game/utils'
import Equipped from '../inventory/Equipped.jsx'

const Stats = ({stats}) => {

    const renderStat = (label, path) => {
        const value = stats.getIn(path)

        return (
            <tr key={path.join('.')} className={value === 0 ? 'text-muted' : null}>
                <th className="text-right">{label}</th>
                <td>{value}</td>
            </tr>
        )
    }

    return (
        <div>
            <h4>Player character</h4>
            <table className="scarl-table">
                <tbody>
                    {creatureStats.map(renderStat).toArray()}
                </tbody>
            </table>
        </div>
    )
}

const PlayerInfo = ({equipments, inventory, kinds, player}) => {
    const stats = fromJS(addStats(player.creature.stats, player.equipmentStats))

    return (
        <div>
            <div className="scarl-panel">
                <Stats stats={stats} />
            </div>
            <div className="scarl-panel">
                <Equipped
                    equipments={equipments}
                    inventory={inventory}
                    kinds={kinds.items} />
            </div>
        </div>
    )
}

export default PlayerInfo
