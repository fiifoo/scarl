import { TILE_SIZE } from '../const'
import { clearContext, createCanvas } from '../utils'

const SIZE_ADJUST = 3

export default (area, kinds) => {
    const canvas = createCanvas(area)
    const context = canvas.getContext('2d')

    const clear = () => clearContext(area, context)

    const update = player => {
        const location = player.creature.location
        const color = kinds.creatures.get(player.creature.kind).color

        clear()
        context.strokeStyle = color
        const x = location.x * TILE_SIZE + SIZE_ADJUST + 0.5
        const y = location.y * TILE_SIZE + TILE_SIZE + 0.5

        context.beginPath()
        context.moveTo(x, y)
        context.lineTo(x + TILE_SIZE - SIZE_ADJUST * 2, y)
        context.stroke()
    }

    return {
        canvas,
        update,
    }
}
