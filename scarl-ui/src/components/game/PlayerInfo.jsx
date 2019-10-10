import React from 'react'
import { fromJS } from 'immutable'
import Stats from '../../game/Stats'
import { getPlayerStats } from '../../game/utils'
import Equipped from '../inventory/Equipped.jsx'

const Player = ({character, stats}) => {

    const renderStat = (label, path) => {
        const value = stats.getIn(path)

        return (
            <tr key={path.join('.')} className={Stats.isEmpty(path)(value) ? 'text-muted' : null}>
                <th className="text-right">{label}</th>
                <td className="description">{Stats.getDisplayValue(path)(value)}</td>
            </tr>
        )
    }

    return (
        <div>
            <h4>Player character</h4>
            <table className="scarl-table">
                <tbody>
                    <tr>
                        <th className="text-right">Level</th>
                        <td>{character.level}</td>
                    </tr>
                    <tr>
                        <th className="text-right">Experience</th>
                        <td>{character.experience}</td>
                    </tr>
                    {Stats.stats.map(renderStat).toArray()}
                </tbody>
            </table>
        </div>
    )
}

const PlayerInfo = ({equipmentSet, equipments, inventory, kinds, player, setEquipmentSet}) => {
    const stats = fromJS(getPlayerStats(player))

    return (
        <div>
            <div className="scarl-panel">
                <Player character={player.creature.character} stats={stats} />
            </div>
            <div className="scarl-panel">
                <Equipped
                    equipmentSet={equipmentSet}
                    equipments={equipments}
                    inventory={inventory}
                    kinds={kinds.items}
                    setEquipmentSet={setEquipmentSet} />
            </div>
        </div>
    )
}

export default PlayerInfo
