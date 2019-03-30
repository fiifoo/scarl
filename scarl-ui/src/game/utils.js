import { fromJS } from 'immutable'
import { stats } from './creature'
import { distance, line } from './geometry'

export const addStats = (a, b) => {
    a = fromJS(a)
    b = fromJS(b)

    return stats.reduce((a, _, stat) => (
        a.setIn(stat, a.getIn(stat) + b.getIn(stat))
    ), a).toJS()
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
    const stats = addStats(player.creature.stats, player.equipmentStats)
    const consumption = stats[type].consumption

    return getShortage(player, consumption)
}

export const getCreatureConditionsInfo = creature => creature.conditions.map(getCreatureConditionInfo)

export const getCreatureInfo = (creature, player, factions) => {
    const health = creature.stats.health.max // equipments not supported
    const damage = creature.damage

    const info = [
        getCreatureFactionInfo(creature, player, factions),
        getCreatureWoundedInfo(damage / health),
    ].filter(x => !!x).concat(getCreatureConditionsInfo(creature))

    if (info.length === 0) {
        return undefined
    } else {
        return info.join(', ')
    }
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

export const getLocationSummary = (factions, fov, map, kinds, player) => location => {
    const content = getLocationKinds(location, fov, map)

    if (content === undefined) {
        return undefined
    }

    const creatures = getLocationCreatures(location, fov).filter(x => x.id !== player.creature.id).map(getCreatureDescription(player, factions, kinds))
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

export const getMissileLauncherRange = player => {
    const creature = player.creature.stats
    const equipment = player.equipmentStats

    return creature.launcher.missile || equipment.launcher.missile ? (
        creature.launcher.range + equipment.launcher.range
    ) : 0
}

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

export const isEnemyChecker = (player, factions) => {
    const enemyFactions = factions.get(player.creature.faction).enemies

    return creature => enemyFactions.contains(creature.faction)
}

// keys: Set
export const isLocked = keys => lockable => {
    const check = lock => (
        ! lock.key || ! keys.contains(fromJS(lock.key)) || (lock.sub && check(lock.sub))
    )

    return lockable.locked && check(lockable.locked)
}

export const seekTargets = (player, factions, fov, missile = false) => {
    const location = player.creature.location
    const range = missile ? getMissileLauncherRange(player) : getRangedAttackRange(player)
    const isEnemy = isEnemyChecker(player, factions)

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

const getCreatureConditionInfo = condition => {
    switch (condition.key) {
        default: {
            return condition.key
        }
    }
}

const getCreatureDescription = (player, factions, kinds) => creature => {
    const kind = kinds.creatures.get(creature.kind)
    const info = getCreatureInfo(creature, player, factions)

    if (info) {
        return `${kind.name} (${info})`
    } else {
        return kind.name
    }
}

const getCreatureFactionInfo = (creature, player, factions) => {
    if (isEnemyChecker(player, factions)(creature)) {
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
