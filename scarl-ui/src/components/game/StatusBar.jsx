import React from 'react'

const sectionStyle = {
    display: 'table-cell',
    paddingRight: 20,
}

const getAlert = (value, max) => value / max < 0.5 ? value / max < 0.25 ? 'text-danger' : 'text-warning' : null

const StatusBar = ({player}) => {
    const creature = player.creature
    const creatureStats = creature.stats
    const equipmentStats = player.equipmentStats

    const healthMax = creatureStats.health.max + equipmentStats.health.max
    const energyMax = creatureStats.energy.max + equipmentStats.energy.max
    const materialsMax = creatureStats.materials.max + equipmentStats.materials.max

    const health = healthMax - Math.floor(creature.damage)
    const energy = Math.floor(creature.energy)
    const materials = Math.floor(creature.materials)

    const healthAlert = getAlert(health, healthMax)
    const energyAlert = getAlert(energy, energyMax)
    const materialsAlert = getAlert(materials, materialsMax)

    return (
        <div>
            <div style={sectionStyle} className={healthAlert}>Health <b>{health}/{healthMax}</b></div>
            <div style={sectionStyle} className={energyAlert}>Energy <b>{energy}/{energyMax}</b></div>
            <div style={sectionStyle} className={materialsAlert}>Materials <b>{materials}/{materialsMax}</b></div>
        </div>
    )
}

const StatusBarIfSet = ({player}) => player ? <StatusBar player={player} /> : <div />

export default StatusBarIfSet
