import SolarSystem from '../../SolarSystem'
import { clearContext, createCanvas, createDraw } from '../utils'

const LIMIT = 3600 * 24 * 100
const TICK = 3600 * 24

export default (spaceships, stellarBodies) => {
    const {canvas, context} = createCanvas()

    context.globalAlpha = 0.2
    const draw = createDraw(context)

    const renderBody = body => {
        const source = stellarBodies.get(body.source)

        draw.dot(source.color, 2)(body.position.x, body.position.y)
    }

    const renderShip = ship => {
        const source = spaceships.get(ship.source)

        draw.dot(source.color, 1)(ship.travel.position.x, ship.travel.position.y)
    }

    const clear = () => clearContext(context)

    const update = system => {
        clear()

        const start = system.time

        while (system.time < start + LIMIT) {
            system = SolarSystem.tick(TICK)(system)
            system.bodies.forEach(renderBody)
            system.ships.filter(x => !! x.travel).forEach(renderShip)
        }
    }

    return {
        canvas,
        clear,
        update,
    }
}
