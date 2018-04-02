import { HIGHLIGHT_COLOR } from '../const'
import { clearContext, createCanvas, createDraw } from '../utils'

export default area => {
    const canvas = createCanvas(area)
    const context = canvas.getContext('2d')

    const clear = () => clearContext(area, context)

    const update = locations => {
        clear()

        locations.forEach(render)
    }

    const render = createDraw(context).fill(HIGHLIGHT_COLOR)

    return {
        canvas,
        update,
    }
}
