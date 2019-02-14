import SolarSystem from '../../SolarSystem'
import { clearContext, createCanvas, createDraw } from '../utils'

const INTERVAL = 10

export default (spaceships, stellarBodies, clearTravel) => {
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

    const update = (system, ui) => {
        const ship = ui.travel.ship
        const travel = ui.travel.travel

        if (! travel) {
            clearTravel()

            return
        }

        const path = ['ships', ship, 'travel']
        system = system.setIn(path, travel)

        const tick = () => {
            system = SolarSystem.tick()(system)

            clear()
            system.bodies.forEach(renderBody)
            system.ships.filter(x => !! x.travel).forEach(renderShip)

            if (! system.getIn(path)) {
                clear()
                clearInterval(interval)
                clearTravel()
            }
        }

        const interval = setInterval(tick, INTERVAL)
    }

    return {
        canvas,
        clear,
        update,
    }
}
