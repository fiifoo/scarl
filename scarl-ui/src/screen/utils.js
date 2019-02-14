import { Record } from 'immutable'
import { SCREEN_MARGIN, TILE_MIDDLE, TILE_SIZE } from './const'

export const ScreenOffset = Record({
    x: 0,
    y: 0,
})

export const calculateScreenOffset = (area, viewSize, offset, player) => {
    if (viewSize === null) {
        return ScreenOffset()
    }

    const location = player.creature.location

    return ScreenOffset({
        x: calculateScreenOffsetDimension(area.width, viewSize.width, offset.x, location.x),
        y: calculateScreenOffsetDimension(area.height, viewSize.height, offset.y, location.y),
    })
}

// Has weird recalculation to fix issues with smaller views
const calculateScreenOffsetDimension = (areaSize, viewSize, current, location) => {
    const size = Math.floor(viewSize / TILE_SIZE)
    const margin = size < SCREEN_MARGIN * 2 ? Math.ceil(size / 2) : SCREEN_MARGIN

    let offset = current

    if (location - offset < margin) {
        const next = location - margin
        offset = next - (next % margin)

        if (location - offset > size - margin) {
            offset = location - size + margin
        }
    } else if (location - offset > size - margin) {
        const next = location - size + margin
        offset = next + (margin - next % margin)

        if (location - offset < margin) {
            offset = location - margin
        }
    }

    if (offset + size > areaSize) {
        offset = areaSize - size
    }
    if (offset < 0) {
        offset = 0
    }

    return offset
}

export const createCanvas = area => {
    const canvas = document.createElement('canvas')
    canvas.width = area.width * TILE_SIZE
    canvas.height = area.height * TILE_SIZE

    return canvas
}

export const clearContext = (area, context) => {
    context.clearRect(0, 0, area.width * TILE_SIZE, area.height * TILE_SIZE)
}

export const createDraw = context => ({

    dot: color => location => {
        context.fillStyle = color

        const x = location.x * TILE_SIZE + TILE_MIDDLE + 0.5
        const y = location.y * TILE_SIZE + TILE_MIDDLE + 0.5

        context.beginPath()
        context.moveTo(x, y)
        context.arc(x, y, 3, 0, Math.PI * 2)
        context.fill()
    },

    fill: color =>  location => {
        context.fillStyle = color

        const x = location.x * TILE_SIZE
        const y = location.y * TILE_SIZE

        context.fillRect(x, y, TILE_SIZE, TILE_SIZE)
    },

    line: color => from => to => {
        context.strokeStyle = color

        const from_x = from.x * TILE_SIZE + TILE_MIDDLE + 0.5
        const from_y = from.y * TILE_SIZE + TILE_MIDDLE + 0.5
        const to_x = to.x * TILE_SIZE + TILE_MIDDLE + 0.5
        const to_y = to.y * TILE_SIZE + TILE_MIDDLE + 0.5

        context.beginPath()
        context.moveTo(from_x, from_y)
        context.lineTo(to_x, to_y)
        context.stroke()
    },
})

export const getPixelLocation = (x, y) => ({
    x: Math.floor(x / TILE_SIZE),
    y: Math.floor(y / TILE_SIZE),
})

const getRandomColorChannel = () => Math.round(Math.random() * 255).toString()

export const getRandomColor = alpha => {
    const r = getRandomColorChannel()
    const g = getRandomColorChannel()
    const b = getRandomColorChannel()

    return `rgba(${r}, ${g}, ${b}, ${alpha})`
}
