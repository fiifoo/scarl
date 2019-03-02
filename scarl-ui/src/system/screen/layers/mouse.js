import { Record } from 'immutable'
import { CENTER_RETICULE_COLOR } from '../const'
import { clearContext, createCanvas, getPixelPosition } from '../utils'

const State = Record({
    systemView: undefined,
    viewSize: undefined,
})

export default (setSystemViewCenter, setSystemViewScale) => {
    const {canvas, context, draw} = createCanvas()

    context.strokeStyle = CENTER_RETICULE_COLOR
    context.globalAlpha = 0.5
    context.lineWidth = 1

    let state = State()

    canvas.addEventListener('click', event => {
        const position = getMousePosition(event, state.systemView)

        setSystemViewCenter(position)
    })

    canvas.addEventListener('wheel', event => {
        const modifier = event.deltaY > 0 ? 1 / 1.5 : 1.5

        const scale = state.systemView.scale * modifier

        setSystemViewScale(scale)
    })

    const getMousePosition = event => {
        const rect = canvas.getBoundingClientRect()

        const x = event.clientX - rect.left
        const y = event.clientY - rect.top

        return getPixelPosition(state.systemView, state.viewSize)(x, y)
    }

    const clear = () => clearContext(context)

    const update = (world, ui, viewSize) => {
        state = State({
            systemView: ui.systemView,
            viewSize,
        })

        clear()

        const x = viewSize.width / 2
        const y = viewSize.height / 2

        context.resetTransform()

        context.beginPath()
        context.moveTo(x - 10, y)
        context.lineTo(x + 10, y)
        context.stroke()

        context.beginPath()
        context.moveTo(x, y - 10)
        context.lineTo(x, y + 10)
        context.stroke()
    }

    return {
        canvas,
        context,
        draw,
        update,
    }
}
