import Position from '../Position'
import { PIXEL_SIZE, SCREEN_SIZE } from './const'

export const createCanvas = () => {
    const canvas = document.createElement('canvas')
    canvas.width = SCREEN_SIZE
    canvas.height = SCREEN_SIZE

    const context = canvas.getContext('2d')
    context.lineWidth = 3

    return {canvas, context}
}

export const clearContext = context => {
    context.save()
    context.resetTransform()

    context.clearRect(0, 0, SCREEN_SIZE, SCREEN_SIZE)

    context.restore()
}

export const transformContext = ({center, scale}, {width, height}) => context => {
    context.resetTransform()

    context.translate(
        width / 2 - center.x / PIXEL_SIZE * scale,
        height / 2 - center.y / PIXEL_SIZE * scale
    )
    context.scale(scale, scale)
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

export const getPixelPosition = ({center, scale}, {width, height}) => (x, y) => {
    const offset = {
        x: width / 2 - center.x / PIXEL_SIZE * scale,
        y: height / 2 - center.y / PIXEL_SIZE * scale,
    }

    return Position({
        x: (x - offset.x) * PIXEL_SIZE / scale,
        y: (y - offset.y) * PIXEL_SIZE / scale,
    })
}
