import { clearContext, createCanvas, createDraw } from '../utils'

export default (spaceships, stellarBodies) => {
    const {canvas, context} = createCanvas()

    const draw = createDraw(context)

    const drawBody = draw.dot(3)
    const drawShip = draw.dot(2)

    const renderBody = body => {
        const source = stellarBodies.get(body.source)

        drawBody(source.color)(body.position.x, body.position.y)
    }

    const renderShip = ship => {
        const source = spaceships.get(ship.source)

        drawShip(source.color)(ship.travel.position.x, ship.travel.position.y)
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
