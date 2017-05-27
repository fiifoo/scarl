import { HIGHLIGHT_COLOR, TILE_SIZE } from '../const'
import { clearContext, createCanvas } from '../utils'

export default () => {
    const canvas = createCanvas()
    const context = canvas.getContext('2d')
    context.fillStyle = HIGHLIGHT_COLOR

    const clear = () => clearContext(context)

    const update = locations => {
        clear()

        locations.forEach(render)
    }

    const render = location => {
        const x = location.x * TILE_SIZE
        const y = location.y * TILE_SIZE

        context.fillRect(x, y, TILE_SIZE, TILE_SIZE)
    }

    return {
        canvas,
        clear,
        update,
    }
}
