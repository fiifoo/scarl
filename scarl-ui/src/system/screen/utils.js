import { PIXEL_SIZE, SCREEN_SIZE } from './const'

export const createCanvas = () => {
    const canvas = document.createElement('canvas')
    canvas.width = SCREEN_SIZE
    canvas.height = SCREEN_SIZE

    const context = canvas.getContext('2d')
    context.translate(SCREEN_SIZE / 2, SCREEN_SIZE / 2)

    return {canvas, context}
}

export const clearContext = context => {
    context.clearRect(- SCREEN_SIZE / 2, - SCREEN_SIZE / 2, SCREEN_SIZE, SCREEN_SIZE)
}

export const createDraw = context => ({
    dot: radius => color => (x, y) => {
        context.fillStyle = color

        context.moveTo(x / PIXEL_SIZE, y / PIXEL_SIZE)
        context.beginPath()
        context.arc(x / PIXEL_SIZE, y / PIXEL_SIZE, radius, 0, Math.PI * 2)
        context.fill()
    },

    circle: radius => color => (x, y) => {
        context.strokeStyle = color

        context.moveTo(x / PIXEL_SIZE, y / PIXEL_SIZE)
        context.beginPath()
        context.arc(x / PIXEL_SIZE, y / PIXEL_SIZE, radius, 0, Math.PI * 2)
        context.stroke()
    },

    line: color => (from_x, from_y) => (to_x, to_y) => {
        context.strokeStyle = color

        context.beginPath()
        context.moveTo(from_x / PIXEL_SIZE, from_y / PIXEL_SIZE)
        context.lineTo(to_x / PIXEL_SIZE, to_y / PIXEL_SIZE)
        context.stroke()
    },
})

export const getPixelPosition = (x, y) => ({
    x: Math.floor(x * PIXEL_SIZE),
    y: Math.floor(y * PIXEL_SIZE),
})
