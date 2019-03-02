import { clearContext, createCanvas } from '../utils'

export default (spaceships) => {
    const {canvas, context, draw} = createCanvas()

    context.globalAlpha = 0.5

    const drawBodyIndicator = draw.circle(10)

    const clear = () => clearContext(context)

    const renderRoute = color => (from ,to) => {
        draw.line(color)(from.x, from.y)(to.x, to.y)
    }

    const renderDestinationBodyIndicator = color => body => {
        drawBodyIndicator(color)(body.position.x, body.position.y)
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
        context,
        draw,
        update,
    }
}
