import './PlayerInfo.css'
import { addStats } from '../../game/utils'

import React from 'react'

const PlayerInfo = ({player}) => {
    const stats = addStats(player.creature.stats, player.equipmentStats)

    return (
        <div className="player-info">
            <div>Health <b>{stats.health.max - Math.floor(player.creature.damage)}/{stats.health.max}</b></div>
            <div>Energy <b>{Math.floor(player.creature.energy)}/{stats.energy.max}</b></div>
            <div>Materials <b>{Math.floor(player.creature.materials)}/{stats.materials.max}</b></div>
            <div>Level {player.creature.character.level}</div>
            <div>Experience {player.creature.character.experience}</div>
            <div>Attack {stats.melee.attack}</div>
            <div>Damage {stats.melee.damage}</div>
            <div>Ranged attack {stats.ranged.attack}</div>
            <div>Ranged damage {stats.ranged.damage}</div>
            <div>Range {stats.ranged.range}</div>
            <div>Defence {stats.defence}</div>
            <div>Armor {stats.armor}</div>
            <div>Sight {stats.sight.range}</div>
            <div>Speed {stats.speed}</div>
        </div>
    )
}

const PlayerInfoIfSet = ({player}) => player ? <PlayerInfo player={player} /> : <div></div>

export default PlayerInfoIfSet
