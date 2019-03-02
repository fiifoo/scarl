import Position from '../Position'
import { PIXEL_SIZE, SCREEN_SIZE } from './const'

export const createCanvas = () => {
    const canvas = document.createElement('canvas')
    canvas.width = SCREEN_SIZE
    canvas.height = SCREEN_SIZE

    const context = canvas.getContext('2d')
    context.lineWidth = 2

    const draw = createDraw(context)

    return {canvas, context, draw}
}

export const clearContext = context => {
    context.save()
    context.resetTransform()

    context.clearRect(0, 0, SCREEN_SIZE, SCREEN_SIZE)

    context.restore()
}

export const transformLayer = ({center, scale}, {width, height}) => layer => {
    layer.context.resetTransform()

    layer.context.translate(
        width / 2 - center.x / PIXEL_SIZE * scale,
        height / 2 - center.y / PIXEL_SIZE * scale
    )
    layer.draw.scale = scale
}

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

const createDraw = context => {
    const self = {
        scale: 1,
        dot: radius => color => (x, y) => {
            context.fillStyle = color

            context.moveTo(self.pixel(x), self.pixel(y))
            context.beginPath()
            context.arc(self.pixel(x), self.pixel(y), radius, 0, Math.PI * 2)
            context.fill()
        },

        circle: radius => color => (x, y) => {
            context.strokeStyle = color

            context.moveTo(self.pixel(x), self.pixel(y))
            context.beginPath()
            context.arc(self.pixel(x), self.pixel(y), radius, 0, Math.PI * 2)
            context.stroke()
        },

        line: color => (from_x, from_y) => (to_x, to_y) => {
            context.strokeStyle = color

            context.beginPath()
            context.moveTo(self.pixel(from_x), self.pixel(from_y))
            context.lineTo(self.pixel(to_x), self.pixel(to_y))
            context.stroke()
        },

        pixel: coord => coord / PIXEL_SIZE * self.scale
    }

    return self
}
