import { COLS, ROWS, TILE_SIZE } from './const'

export const createCanvas = () => {
    const canvas = document.createElement('canvas')
    canvas.width = COLS * TILE_SIZE
    canvas.height = ROWS * TILE_SIZE

    return canvas
}

export const clearContext = context => {
    context.clearRect(0, 0, COLS * TILE_SIZE, ROWS * TILE_SIZE)
}
