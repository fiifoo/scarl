import { clearContext, createCanvas, createDraw } from '../utils'

export default (spaceships) => {
    const {canvas, context} = createCanvas()

    context.globalAlpha = 0.5
    const draw = createDraw(context)

    const clear = () => clearContext(context)

    const update = (system, ui) => {
        clear()

        if (! ui.travel || ! ui.travel.possible || ! ui.travel.ship) {
            return
        }

        const color = spaceships.get(ui.travel.ship).color
        const from = ui.travel.travel.from
        const to = ui.travel.travel.to

        draw.line(color)(from.x, from.y)(to.x, to.y)
    }

    return {
        canvas,
        clear,
        update,
    }
}
