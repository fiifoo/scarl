import { List } from 'immutable'
import { distance, line } from './geometry'

export const calculateTrajectory = (player, location, fov) => {
    const from = player.creature.location
    const range = getRangedAttackRange(player)
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
            entities.creature
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

export const getLocationConduit = (location, fov) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.conduit : undefined
}

export const getLocationCreature = (location, fov) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.creature : undefined
}

export const getLocationDoor = (location, fov) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.items.find(item => !!item.door) : undefined
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

    const creature = content.creature ? kinds.creatures.get(content.creature).name : undefined
    const wall = content.wall ? kinds.walls.get(content.wall).name : undefined
    const items = content.items.map(item => kinds.items.get(item).name)

    const descriptions = [creature, wall].filter(x => x !== undefined).concat(items)

    return List(descriptions.map (d => d + '.'))
}

export const getLocationUsableCreature = (location, fov) => {
    const creature = getLocationCreature(location, fov)

    if (creature && creature.usable) {
        return creature
    } else {
        return undefined
    }
}

export const getLocationUsableItem = (location, fov) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.items.find(item => item.usable) : undefined
}

export const isEnemyChecker = (player, factions) => {
    const enemyFactions = factions.get(player.creature.faction).enemies

    return creature => enemyFactions.contains(creature.faction)
}

export const seekTargets = (player, factions, fov) => {
    const location = player.creature.location
    const range = getRangedAttackRange(player)
    const isEnemy = isEnemyChecker(player, factions)

    const targets = []
    for (let x = location.x - range; x <= location.x + range; x++) {
        for (let y = location.y - range; y <= location.y + range; y++) {
            const creature = getLocationCreature({x, y}, fov)
            if (creature && isEnemy(creature)) {
                targets.push(creature)
            }
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
            creature: entities.creature ? entities.creature.kind : undefined,
            items: entities.items.map (item => item.kind),
            terrain: entities.terrain ? entities.terrain.kind : undefined,
            wall: entities.wall ? entities.wall.kind : undefined,
        }
    } else {
        return map[l.x] ? map[l.x][l.y] : undefined
    }
}

const getRangedAttackRange = player => player.creature.stats.ranged.range + player.equipmentStats.ranged.range
