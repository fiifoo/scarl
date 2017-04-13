import './PlayerInfo.css'

import React from 'react'

const PlayerInfo = ({player}) => ! player ? <div></div> : (
    <div className="player-info">
        <div>Health <b>{player.stats.health - player.damage}/{player.stats.health}</b></div>
        <div>Experience {player.experience}</div>
        <div>Attack {player.stats.attack}</div>
        <div>Defence {player.stats.defence}</div>
        <div>Damage {player.stats.damage}</div>
        <div>Armor {player.stats.armor}</div>
    </div>
)

export default PlayerInfo
