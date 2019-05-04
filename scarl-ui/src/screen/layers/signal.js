import { Range } from 'immutable'
import { circle, distance } from '../../game/geometry'
import { GRID_COLOR, SIGNAL_COLORS, TILE_SIZE } from '../const'
import { clearContext, createCanvas, createDraw } from '../utils'

const FALLBACK_COLOR = '#000'
const MAX_STRENGTH = 50

export const renderSignal = draw => signal => {
    const color = SIGNAL_COLORS[signal.kind] || FALLBACK_COLOR

    circle(signal.location, signal.radius).forEach(location => {
        const multiplier = 1 - distance(signal.location, location) / signal.radius
        const alpha = signal.strength / MAX_STRENGTH * multiplier

        draw.fill(color, Math.max(0.1, Math.min(alpha, 0.5)))(location)
    })
}

export default area => {
    const canvas = createCanvas(area)
    const context = canvas.getContext('2d')
    const draw = createDraw(context)

    const clear = () => clearContext(area, context)

    const update = signals => {
        clear()

        if (signals) {
            renderGrid()
            signals.forEach(renderSignal(draw))
        }
    }

    const renderGrid = () => {
        context.strokeStyle = GRID_COLOR

        Range(1, area.height).forEach(y => {
            context.beginPath()
            context.moveTo(0.5, y * TILE_SIZE + 0.5)
            context.lineTo(area.width * TILE_SIZE + 0.5, y * TILE_SIZE + 0.5)
            context.stroke()
        })
        Range(1, area.width).forEach(x => {
            context.beginPath()
            context.moveTo(x * TILE_SIZE + 0.5, 0.5)
            context.lineTo(x * TILE_SIZE + 0.5, area.height * TILE_SIZE + 0.5)
            context.stroke()
        })
    }

    return {
        canvas,
        update,
    }
}
