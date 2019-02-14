import { clearContext, createCanvas, createDraw } from '../utils'

export default (spaceships) => {
    const {canvas, context} = createCanvas()

    context.globalAlpha = 0.5
    const draw = createDraw(context)

    const clear = () => clearContext(context)

    const renderRoute = color => (from ,to) => {
        draw.line(color)(from.x, from.y)(to.x, to.y)
    }

    const renderDestinationBodyIndicator = color => body => {
        draw.circle(9)(color)(body.position.x, body.position.y)
        draw.circle(10)(color)(body.position.x, body.position.y)
    }

    const update = (world, ui) => {
        clear()

        if (! ui.travel || ! ui.travel.possible || ! ui.travel.ship) {
            return
        }

        const color = spaceships.get(ui.travel.ship).color
        const from = ui.travel.travel.from
        const to = ui.travel.travel.to
        const destination = world.system.bodies.get(ui.travel.travel.destination)

        renderRoute(color)(from, to)

        if (! ui.travel.simulate) {
            renderDestinationBodyIndicator(color)(destination)
        }
    }

    return {
        canvas,
        clear,
        update,
    }
}
