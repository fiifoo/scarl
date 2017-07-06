import { COLS, ROWS, TILE_MIDDLE, TILE_SIZE } from './const'

export const createCanvas = () => {
    const canvas = document.createElement('canvas')
    canvas.width = COLS * TILE_SIZE
    canvas.height = ROWS * TILE_SIZE

    return canvas
}

export const clearContext = context => {
    context.clearRect(0, 0, COLS * TILE_SIZE, ROWS * TILE_SIZE)
}

export const createDraw = context => ({

    dot: color => {
        context.fillStyle = color

        return location => {
            const x = location.x * TILE_SIZE + TILE_MIDDLE + 0.5
            const y = location.y * TILE_SIZE + TILE_MIDDLE + 0.5

            context.beginPath()
            context.moveTo(x, y)
            context.arc(x, y, 3, 0, Math.PI * 2)
            context.fill()
        }
    },

    fill: color => {
        context.fillStyle = color

        return location => {
            const x = location.x * TILE_SIZE
            const y = location.y * TILE_SIZE

            context.fillRect(x, y, TILE_SIZE, TILE_SIZE)
        }
    },

    line: color => {
        context.strokeStyle = color

        return from => to => {
            const from_x = from.x * TILE_SIZE + TILE_MIDDLE + 0.5
            const from_y = from.y * TILE_SIZE + TILE_MIDDLE + 0.5
            const to_x = to.x * TILE_SIZE + TILE_MIDDLE + 0.5
            const to_y = to.y * TILE_SIZE + TILE_MIDDLE + 0.5

            context.beginPath()
            context.moveTo(from_x, from_y)
            context.lineTo(to_x, to_y)
            context.stroke()
        }
    },
})

const getRandomColorChannel = () => Math.round(Math.random() * 255).toString()

export const getRandomColor = alpha => {
    const r = getRandomColorChannel()
    const g = getRandomColorChannel()
    const b = getRandomColorChannel()

    return `rgba(${r}, ${g}, ${b}, ${alpha})`
}
