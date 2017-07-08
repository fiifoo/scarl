import { HIGHLIGHT_COLOR } from '../const'
import { clearContext, createCanvas, createDraw } from '../utils'

export default () => {
    const canvas = createCanvas()
    const context = canvas.getContext('2d')

    const clear = () => clearContext(context)

    const update = locations => {
        clear()

        locations.forEach(render)
    }

    const render = createDraw(context).fill(HIGHLIGHT_COLOR)

    return {
        canvas,
        clear,
        update,
    }
}
