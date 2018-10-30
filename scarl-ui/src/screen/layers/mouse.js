import { HIGHLIGHT_COLOR } from '../const'
import { clearContext, createCanvas, createDraw, getPixelLocation } from '../utils'

export default (area, autoMove, look, focusKeyboard) => {
    const canvas = createCanvas(area)
    const context = canvas.getContext('2d')

    const clear = () => clearContext(area, context)

    canvas.addEventListener('click', event => {
        const location = getMouseLocation(event)

        autoMove(location)
    })

    canvas.addEventListener('contextmenu', event => {
        const location = getMouseLocation(event)

        look(location)

        focusKeyboard()
        event.preventDefault()
        return false
    })

    canvas.addEventListener('mousemove', event => {
        const location = getMouseLocation(event)

        clear()
        render(location)
    })

    canvas.addEventListener('mouseleave', clear)

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
