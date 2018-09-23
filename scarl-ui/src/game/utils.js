import { fromJS, List } from 'immutable'
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

export const getLocationDescriptions = (location, fov, map, kinds) => {
    const content = getLocationKinds(location, fov, map)

    if (content === undefined) {
        return List()
    }

    const creatures = content.creatures ? content.creatures.map(creature => kinds.creatures.get(creature).name) : []
    const walls = content.wall ? [kinds.walls.get(content.wall).name] : []
    const items = content.items.map(item => kinds.items.get(item).name)

    const descriptions = creatures.concat(walls).concat(items)

    return List(descriptions.map (d => d + '.'))
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

const getLocationKinds = (l, fov, map) => {
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
