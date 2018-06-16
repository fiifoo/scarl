import React from 'react'

const sectionStyle = {
    display: 'table-cell',
    paddingRight: 20,
}

const StatusBar = ({player}) => {
    const creature = player.creature
    const creatureStats = creature.stats
    const equipmentStats = player.equipmentStats

    const healthMax = creatureStats.health.max + equipmentStats.health.max
    const energyMax = creatureStats.energy.max + equipmentStats.energy.max
    const materialsMax = creatureStats.materials.max + equipmentStats.materials.max

    const health = healthMax - Math.floor(creature.damage)
    const healthAlert = health / healthMax < 0.5 ? health / healthMax < 0.25 ? 'text-danger' : 'text-warning' : null

    return (
        <div>
            <div style={sectionStyle} className={healthAlert}>Health <b>{health}/{healthMax}</b></div>
            <div style={sectionStyle}>Energy <b>{Math.floor(creature.energy)}/{energyMax}</b></div>
            <div style={sectionStyle}>Materials <b>{Math.floor(creature.materials)}/{materialsMax}</b></div>
        </div>
    )
}

const StatusBarIfSet = ({player}) => player ? <StatusBar player={player} /> : <div />

export default StatusBarIfSet
