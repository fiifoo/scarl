import { HIGHLIGHT_COLOR } from '../const'
import { clearContext, createCanvas, createDraw, getPixelLocation } from '../utils'

export default (area, autoMove) => {
    const canvas = createCanvas(area)
    const context = canvas.getContext('2d')

    const clear = () => clearContext(area, context)

    canvas.addEventListener('click', event => {
        const location = getMouseLocation(event)

        autoMove(location)
    })

    canvas.addEventListener('mousemove', event => {
        const location = getMouseLocation(event)

        clear()
        render(location)
    })

    const render = createDraw(context).fill(HIGHLIGHT_COLOR)

    const getMouseLocation = event => {
        const rect = canvas.getBoundingClientRect()

        const x = event.clientX - rect.left
        const y = event.clientY - rect.top

        return getPixelLocation(x, y)
    }

    return {
        canvas,
    }
}
