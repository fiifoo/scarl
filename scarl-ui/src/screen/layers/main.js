import { DEFAULT_TERRAIN, MAIN_FONT, TILE_SIZE, TILE_MIDDLE } from '../const'
import { createCanvas } from '../utils'

export default (area, kinds) => {
    const canvas = createCanvas(area)
    const context = canvas.getContext('2d')
    context.font = MAIN_FONT
    context.textAlign = 'center'
    context.textBaseline = 'middle'

    const update = fov => {
        fov.delta.forEach((rows, x) => {
            rows.forEach((entities, y) => {
                renderFovTile(x, y, entities)
            })
        })
        fov.shouldHide.forEach(location => {
            const x = location.x
            const y = location.y
            const entities = fov.cumulative[x][y]

            renderFovTile(x, y, entities, false)
        })
    }

    const updateMap = map => {
        map.forEach((rows, x) => {
            rows.forEach((data, y) => {
                renderMapTile(x, y, data)
            })
        })
    }

    const renderFovTile = (x, y, entities, visible = true) => {
        if (visible && entities.creatures.length > 0) {
            renderEntity(x, y, entities.creatures[entities.creatures.length - 1], kinds.creatures)
        } else if (entities.items.length > 0) {
            renderEntity(x, y, entities.items[entities.items.length - 1], kinds.items)
        } else if (entities.wall) {
            renderEntity(x, y, entities.wall, kinds.walls)
        } else if (entities.terrain) {
            renderEntity(x, y, entities.terrain, kinds.terrains)
        } else {
            renderEmpty(x, y)
        }
    }

    const renderMapTile = (x, y, data) => {
        if (data.items.length > 0) {
            renderKind(x, y, data.items[data.items.length - 1], kinds.items)
        } else if (data.wall) {
            renderKind(x, y, data.wall, kinds.walls)
        } else if (data.terrain) {
            renderKind(x, y, data.terrain, kinds.terrains)
        } else {
            renderEmpty(x, y)
        }
    }

    const renderEntity = (x, y, entity, kindsBranch) => {
        renderKind(x, y, entity.kind, kindsBranch)
    }

    const renderKind = (x, y, kindId, branch) => {
        const kind = branch.get(kindId)
        const display = kind.display
        const color = kind.color

        renderTile(x, y, display, color)
    }

    const renderEmpty = (x, y) => {
        const display = DEFAULT_TERRAIN.display
        const color = DEFAULT_TERRAIN.color

        renderTile(x, y, display, color)
    }

    const renderTile = (x, y, display, color) => {
        const tx = x * TILE_SIZE
        const ty = y * TILE_SIZE

        context.clearRect(tx, ty, TILE_SIZE, TILE_SIZE)

        context.fillStyle = color
        context.fillText(display, tx + TILE_MIDDLE, ty + TILE_MIDDLE)
    }

    return {
        canvas,
        update,
        updateMap,
    }
}
