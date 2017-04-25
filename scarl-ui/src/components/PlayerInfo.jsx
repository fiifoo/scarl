import './PlayerInfo.css'

import React from 'react'

const addStats = (a, b) => {
    return {
        health: a.health + b.health,
        attack: a.attack + b.attack,
        defence: a.defence + b.defence,
        damage: a.damage + b.damage,
        armor: a.armor + b.armor,
        sight: {range: a.sight.range + b.sight.range},
    }
}

const PlayerInfo = ({player}) => {
    const stats = addStats(player.creature.stats, player.equipmentStats)

    return (
        <div className="player-info">
            <div>Health <b>{stats.health - player.creature.damage}/{stats.health}</b></div>
            <div>Level {player.creature.level}</div>
            <div>Experience {player.creature.experience}</div>
            <div>Attack {stats.attack}</div>
            <div>Defence {stats.defence}</div>
            <div>Damage {stats.damage}</div>
            <div>Armor {stats.armor}</div>
            <div>Sight {stats.sight.range}</div>
        </div>
    )
}

const PlayerInfoIfSet = ({player}) => player ? <PlayerInfo player={player} /> : <div></div>

export default PlayerInfoIfSet
