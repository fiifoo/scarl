import './PlayerInfo.css'

import React from 'react'

const addStats = (a, b) => {
    return {
        health: a.health + b.health,
        melee: {
            attack: a.melee.attack + b.melee.attack,
            damage: a.melee.damage + b.melee.damage,
        },
        ranged: {
            attack: a.ranged.attack + b.ranged.attack,
            damage: a.ranged.damage + b.ranged.damage,
            range: a.ranged.range + b.ranged.range,
        },
        defence: a.defence + b.defence,
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
            <div>Attack {stats.melee.attack}</div>
            <div>Damage {stats.melee.damage}</div>
            <div>Ranged attack {stats.ranged.attack}</div>
            <div>Ranged damage {stats.ranged.damage}</div>
            <div>Range {stats.ranged.range}</div>
            <div>Defence {stats.defence}</div>
            <div>Armor {stats.armor}</div>
            <div>Sight {stats.sight.range}</div>
        </div>
    )
}

const PlayerInfoIfSet = ({player}) => player ? <PlayerInfo player={player} /> : <div></div>

export default PlayerInfoIfSet
