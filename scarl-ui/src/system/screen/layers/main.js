import { clearContext, createCanvas, createDraw } from '../utils'

export default (spaceships, stellarBodies) => {
    const {canvas, context} = createCanvas()

    const draw = createDraw(context)

    const renderBody = body => {
        const source = stellarBodies.get(body.source)

        draw.dot(source.color, 3)(body.position.x, body.position.y)
    }

    const renderShip = ship => {
        const source = spaceships.get(ship.source)

        draw.dot(source.color, 2)(ship.travel.position.x, ship.travel.position.y)
    }

    const clear = () => clearContext(context)

    const update = system => {
        clear()

        system.bodies.forEach(renderBody)
        system.ships.filter(x => !! x.travel).forEach(renderShip)
    }

    return {
        canvas,
        clear,
        update,
    }
}
