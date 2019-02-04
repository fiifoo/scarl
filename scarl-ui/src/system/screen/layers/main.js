import { clearContext, createCanvas, createDraw } from '../utils'

export default (spaceships, stellarBodies) => {
    const {canvas, context} = createCanvas()

    const draw = createDraw(context)

    const renderBody = body => {
        const source = stellarBodies.get(body.source)

        draw.dot(source.color)(body.position.x, body.position.y)
    }

    const renderShip = ship => {
        const source = spaceships.get(ship.source)

        draw.dot(source.color)(ship.travel.position.x, ship.travel.position.y)
    }

    const update = system => {
        //clearContext(context)

        system.bodies.forEach(renderBody)
        system.ships.filter(x => !! x.travel).forEach(renderShip)
    }

    return {
        canvas,
        update,
    }
}
