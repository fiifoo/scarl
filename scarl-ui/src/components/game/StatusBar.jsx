import React from 'react'
import { getCreatureConditionsInfo, getCreatureStancesInfo } from '../../game/utils'

const ucFirst = x => x.charAt(0).toUpperCase() + x.slice(1)

const sectionStyle = {
    display: 'table-cell',
    paddingRight: 20,
}

const getAlert = (value, max) => value / max < 0.5 ? value / max < 0.25 ? 'text-danger' : 'text-warning' : null

const StatusBar = ({equipmentSet, hasWorldActions, player, site, openWorld}) => {
    const creature = player.creature
    const creatureStats = creature.stats
    const equipmentStats = player.equipmentStats

    const healthMax = creatureStats.health.max + equipmentStats.health.max
    const energyMax = creatureStats.energy.max + equipmentStats.energy.max
    const materialsMax = creatureStats.materials.max + equipmentStats.materials.max

    const health = healthMax - Math.floor(creature.damage)
    const energy = Math.floor(creature.resources.energy)
    const materials = Math.floor(creature.resources.materials)

    const healthAlert = getAlert(health, healthMax)
    const energyAlert = getAlert(energy, energyMax)
    const materialsAlert = getAlert(materials, materialsMax)

    const weaponsStyle = equipmentSet == 1 ? null : equipmentSet == 2 ? 'text-primary' : 'text-success'

    return (
        <div>
            <div style={sectionStyle} className={healthAlert}>Health <b>{health}/{healthMax}</b></div>
            <div style={sectionStyle} className={energyAlert}>Energy <b>{energy}/{energyMax}</b></div>
            <div style={sectionStyle} className={materialsAlert}>Materials <b>{materials}/{materialsMax}</b></div>
            <div style={sectionStyle} className={weaponsStyle}>Weapons <b>{equipmentSet}</b></div>
            <div style={sectionStyle}><i><b>{site.name}</b></i></div>
            {hasWorldActions && (
                <div style={sectionStyle}><a onClick={() => openWorld()}><i><b>World access</b></i></a></div>
            )}
            {getCreatureStancesInfo(player.creature).map(ucFirst).map((stance, i) => (
                <div style={sectionStyle} className="text-info" key={i}><b>{stance}</b></div>
            ))}
            {getCreatureConditionsInfo(player.creature).map(ucFirst).map((condition, i) => (
                <div style={sectionStyle} className="text-danger" key={i}><b>{condition}</b></div>
            ))}
        </div>
    )
}

const StatusBarIfSet = props => props.player.creature ? <StatusBar {...props} /> : <div />

export default StatusBarIfSet
