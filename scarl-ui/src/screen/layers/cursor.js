import { CURSOR_COLOR, CURSOR_BLINK_INTERVAL, TILE_SIZE } from '../const'
import { clearContext, createCanvas } from '../utils'

export default () => {
    const canvas = createCanvas()
    const context = canvas.getContext('2d')
    context.strokeStyle = CURSOR_COLOR

    const state = {
        cursor: null,
    }

    const clear = () => clearContext(context)

    const update = cursor => {
        clear()

        if (cursor === null) {
            state.cursor = null
        } else {
            state.cursor = cursor
            render(cursor)
        }
    }

    const render = cursor => {
        if (state.cursor !== cursor) {
            return
        }

        const x = cursor.x * TILE_SIZE + 0.5
        const y = cursor.y * TILE_SIZE + TILE_SIZE + 0.5

        context.beginPath()
        context.moveTo(x, y)
        context.lineTo(x + TILE_SIZE, y)
        context.stroke()

        setTimeout(() => blink(cursor), CURSOR_BLINK_INTERVAL)
    }

    const blink = cursor => {
        if (state.cursor !== cursor) {
            return
        }

        clear()
        setTimeout(() => render(cursor), CURSOR_BLINK_INTERVAL)
    }

    return {
        canvas,
        clear,
        update,
    }
}
