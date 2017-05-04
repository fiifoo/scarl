import { List } from 'immutable'
import { distance } from './geometry'

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

export const getLocationPickableItems = (location, fov) => {
    const entities = getLocationEntities(location, fov)

    return entities ? entities.items.filter(item => item.pickable) : undefined
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

export const seekTargets = (player, fov) => {
    const location = player.creature.location
    const range = player.creature.stats.ranged.range + player.equipmentStats.ranged.range

    const targets = []
    for (let x = location.x - range; x <= location.x + range; x++) {
        for (let y = location.y - range; y <= location.y + range; y++) {
            const creature = getLocationCreature({x, y}, fov)
            if (creature && creature.id !== player.creature.id) {
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
