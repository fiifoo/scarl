import { CURRENT_BODY_COLOR } from '../const'
import { clearContext, createCanvas } from '../utils'
import { getCurrentStellarBody } from '../../../game/world'

export default (spaceships, stellarBodies) => {
    const {canvas, context, draw} = createCanvas()

    const drawBody = draw.dot(5)
    const drawShip = draw.dot(3)
    const drawBodyIndicator = draw.circle(10)

    const renderBody = body => {
        const source = stellarBodies.get(body.source)

        drawBody(source.color)(body.position.x, body.position.y)
    }

    const renderShip = ship => {
        const source = spaceships.get(ship.source)

        drawShip(source.color)(ship.travel.position.x, ship.travel.position.y)
    }

    const renderCurrentBodyIndicator = body => {
        drawBodyIndicator(CURRENT_BODY_COLOR)(body.position.x, body.position.y)
    }

    const clear = () => clearContext(context)

    const update = world => {
        clear()

        const currentBody = getCurrentStellarBody(world)

        world.system.bodies.forEach(renderBody)
        world.system.ships.filter(x => !! x.travel).forEach(renderShip)
        currentBody && renderCurrentBodyIndicator(currentBody)
    }

    return {
        canvas,
        context,
        draw,
        update,
    }
}
