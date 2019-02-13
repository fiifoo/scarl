import SolarSystem from '../../SolarSystem'
import { clearContext, createCanvas, createDraw } from '../utils'

const INTERVAL = 20

export default (spaceships, stellarBodies, clearTravelSimulation) => {
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

    const update = (system, ui) => {
        const {shipId, destinationId} = ui.travelSimulation
        const travel = SolarSystem.calculateTravel(shipId, destinationId)(system)

        if (! travel) {
            clearTravelSimulation()

            return
        }

        const path = ['ships', shipId, 'travel']
        system = system.setIn(path, travel)

        const tick = () => {
            system = SolarSystem.tick()(system)

            clear()
            system.bodies.forEach(renderBody)
            system.ships.filter(x => !! x.travel).forEach(renderShip)

            if (! system.getIn(path)) {
                clear()
                clearInterval(interval)
                clearTravelSimulation()
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
