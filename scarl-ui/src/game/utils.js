import { fromJS, Map } from 'immutable'
import { getConditionLabel } from './condition'
import { distance, line } from './geometry'
import { getStanceLabel, isChangeStanceAllowed } from './stance'
import Stats from './Stats'

const SHORT_MESSAGE_LIMIT = 100

const Faction = {
    Friendly: 'Faction.Friendly',
    Neutral: 'Faction.Neutral',
    Hostile: 'Faction.Hostile',
}

export const calculateTrajectory = (player, location, fov, missile = false) => {
    const from = player.creature.location
    const range = missile ? getMissileLauncherRange(player) : getRangedAttackRange(player)
    let blocked = false

    return line(from, location).filter((location, index) => {
        if (index === 0) {
            return true
        }
        if (blocked || index > range) {
            return false
        }

        const entities = getLocationEntities(location, fov)

        if (entities && (
            entities.creatures.length > 0
            || entities.wall
            || entities.items.find(item => item.door && !item.door.open)
        )) {
            blocked = true
        }

        return true
    })
}

export const getAdjacentLocations = l => ([
    {x: l.x - 1, y: l.y - 1},
    {x: l.x - 1, y:  l.y},
    {x: l.x - 1, y:  l.y + 1},
    {x: l.x, y:  l.y + 1},
    {x: l.x + 1, y:  l.y + 1},
    {x: l.x + 1, y:  l.y},
    {x: l.x + 1, y:  l.y - 1},
    {x: l.x, y:  l.y - 1}
])

export const getAttackShortage = (player, type) => {
    const stats = getPlayerStats(player)
    const consumption = stats[type].consumption

    return getShortage(player, consumption)
}

export const getAttackStanceAllowed = (player, type) => {
    const stats = getPlayerStats(player)
    const stance = stats[type].stance

    return ! stance || isChangeStanceAllowed(player.creature, stance)
}

export const getCreatureConditionsInfo = creature => creature.conditions.map(x => getConditionLabel(x.key))

export const getCreatureInfo = (creature, player, factions, area) => {
    const health = creature.stats.health.max // equipments not supported
    const damage = creature.damage

    const info = [
        getCreatureFactionInfo(creature, player, factions, area),
        getCreatureWoundedInfo(damage / health),
    ].filter(x => !!x).concat(getCreatureConditionsInfo(creature)).concat(getCreatureStancesInfo(creature))

    if (info.length === 0) {
        return undefined
    } else {
        return info.join(', ')
    }
}

export const getCreatureStancesInfo = creature => creature.stances.map(x => getStanceLabel(x.key))

export const getEventMessages = (events, short = false) => {
    return events.filter(e => e.data.message !== undefined).map(event => {
        switch (event.type) {
            case 'CommunicationEvent': {
                if (! short || isShortCommunicationEvent(event)) {
                    return event.data.message
                } else {
                    return null
                }
            }
            default: {
                return event.data.message
            }
        }
    }).filter(x => !!x)
}

export const getLocationConduit = (location, fov) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.conduit : undefined
}

export const getLocationCreatures = (location, fov) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.creatures : []
}

export const getLocationDoor = (location, fov) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.items.find(item => !!item.door) : undefined
}

export const getLocationKinds = (l, fov, map) => {
    const entities = getLocationEntities(l, fov)

    if (entities) {
        return {
            creatures: entities.creatures.map(creature => creature.kind),
            items: entities.items.map(item => item.kind),
            terrain: entities.terrain ? entities.terrain.kind : undefined,
            wall: entities.wall ? entities.wall.kind : undefined,
        }
    } else {
        return map[l.x] ? map[l.x][l.y] : undefined
    }
}

// keys: Set
export const getLocationLockedCreatures = (location, fov, keys) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.creatures.filter(isLocked(keys)) : []
}

// keys: Set
export const getLocationLockedItems = (location, fov, keys) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.items.filter(isLocked(keys)) : []
}

export const getLocationPickableItems = (location, fov) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.items.filter(item => item.pickable) : []
}

export const getLocationRecyclableItems = (location, fov) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.items.filter(item => item.recyclable) : []
}

export const getLocationSummary = (factions, area, fov, map, kinds, player) => location => {
    const content = getLocationKinds(location, fov, map)

    if (content === undefined) {
        return undefined
    }

    const creatures = getLocationCreatures(location, fov).filter(x => x.id !== player.creature.id).map(getCreatureDescription(player, factions, area, kinds))
    const items = content.items.map(item => kinds.items.get(item).name)
    const walls = content.wall ? [kinds.walls.get(content.wall).name] : []

    return creatures.concat(items).concat(walls).map(x => x + '.').join(' ') || undefined
}

export const getLocationUsableCreatures = (location, fov, keys) => {
    const creatures = getLocationCreatures(location, fov)

    const isNotLocked = item => ! isLocked(keys)(item)

    return creatures.filter(creature => creature.usable).filter(isNotLocked)
}

export const getLocationUsableItems = (location, fov, keys) => {
    const entities = getLocationEntities(location, fov)

    const isNotLocked = item => ! isLocked(keys)(item)

    return entities ? entities.items.filter(item => item.usable).filter(isNotLocked) : []
}

export const getMissile = state => {
    const missiles = getMissiles(state.player)

    if (missiles.length === 0) {
        return null
    }

    const selected = state.ui.game.missile

    return selected && missiles.indexOf(selected) !== -1 ? selected : missiles[0]
}

export const getMissiles = player => {
    const stats = player.creature.stats
    const equipmentStats = player.equipmentStats

    return stats.launcher.missiles.concat(equipmentStats.launcher.missiles)
}

export const getMissileLauncherRange = player => {
    const creature = player.creature.stats
    const equipment = player.equipmentStats

    return creature.launcher.missiles.length > 0 || equipment.launcher.missiles.length > 0 ? (
        creature.launcher.range + equipment.launcher.range
    ) : 0
}

export const getPlayerStats = player => (
    Stats.add(player.creature.stats, player.equipmentStats)
)

export const getRangedAttackRange = player => {
    const creature = player.creature.stats
    const equipment = player.equipmentStats

    return creature.ranged.range + equipment.ranged.range
}

export const getShortage = (player, consumption) => {
    let energy = false
    let materials = false

    if (consumption.energy > player.creature.resources.energy) {
        energy = true
    }
    if (consumption.materials > player.creature.resources.materials) {
        materials = true
    }
    if (energy || materials) {
        return {
            energy,
            materials
        }
    } else {
        return null
    }
}

export const isCommunicationEvent = event => (
    event.type === 'CommunicationEvent' && ! isShortCommunicationEvent(event)
)

export const isEnemyChecker = (player, factions, area) => {
    const dispositions = factions.get(player.creature.faction).dispositions.merge(
        area.factions.dispositions.get(player.creature.faction, Map())
    )
    const enemyFactions = dispositions.filter(x => x === Faction.Hostile).keySeq().toSet()

    return creature => enemyFactions.contains(creature.faction)
}

// keys: Set
export const isLocked = keys => lockable => {
    const check = lock => (
        ! lock.key || ! keys.contains(fromJS(lock.key)) || (lock.sub && check(lock.sub))
    )

    return lockable.locked && check(lockable.locked)
}

export const seekTargets = (player, factions, area, fov, missile = false) => {
    const location = player.creature.location
    const range = missile ? getMissileLauncherRange(player) : getRangedAttackRange(player)
    const isEnemy = isEnemyChecker(player, factions, area)

    let targets = []
    for (let x = location.x - range; x <= location.x + range; x++) {
        for (let y = location.y - range; y <= location.y + range; y++) {
            targets = targets.concat(getLocationCreatures({x, y}, fov).filter(isEnemy))
        }
    }

    return targets.sort((a, b) => (
        distance(location, a.location) < distance(location, b.location) ? -1 : 1
    ))
}

const getLocationEntities = (l, fov) => fov[l.x] ? fov[l.x][l.y] : undefined

const getCreatureDescription = (player, factions, area, kinds) => creature => {
    const kind = kinds.creatures.get(creature.kind)
    const info = getCreatureInfo(creature, player, factions, area)

    if (info) {
        return `${kind.name} (${info})`
    } else {
        return kind.name
    }
}

const getCreatureFactionInfo = (creature, player, factions, area) => {
    if (isEnemyChecker(player, factions, area)(creature)) {
        return undefined
    } else {
        return 'friendly'
    }
}

const getCreatureWoundedInfo = damagePercentage => {
    if (damagePercentage === 0) {
        return undefined
    } else if (damagePercentage < 0.25) {
        return 'slightly wounded'
    } else if (damagePercentage < 0.75) {
        return 'wounded'
    } else {
        return 'badly wounded'
    }
}

const isShortCommunicationEvent = event => {
    const choices = event.data.choices
    const message = event.data.message

    return choices.length === 0 && message.length < SHORT_MESSAGE_LIMIT && ! message.match(/\r\n|\r|\n/)
}
