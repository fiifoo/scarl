import SolarSystem from '../../SolarSystem'
import { clearContext, createCanvas, createDraw } from '../utils'

const INTERVAL = 10

export default (spaceships, stellarBodies, clearTravel) => {
    const {canvas, context} = createCanvas()

    const draw = createDraw(context)

    const drawBody = draw.dot(5)
    const drawShip = draw.dot(3)
    const drawBodyIndicator = draw.circle(12)

    const renderBody = body => {
        const source = stellarBodies.get(body.source)

        drawBody(source.color)(body.position.x, body.position.y)
    }

    const renderShip = ship => {
        const source = spaceships.get(ship.source)

        drawShip(source.color)(ship.travel.position.x, ship.travel.position.y)
    }

    const renderDestinationBodyIndicator = color => body => {
        drawBodyIndicator(color)(body.position.x, body.position.y)
    }

    const clear = () => clearContext(context)

    const update = (world, ui) => {
        const ship = ui.travel.ship
        const travel = ui.travel.travel

        if (! travel) {
            clearTravel()

            return
        }

        const path = ['ships', ship, 'travel']
        let system = world.system.setIn(path, travel)

        const renderDestination = renderDestinationBodyIndicator(spaceships.get(system.ships.get(ship).source).color)

        const tick = () => {
            system = SolarSystem.tick()(system)

            const destination = system.bodies.get(ui.travel.travel.destination)

            clear()
            system.bodies.forEach(renderBody)
            system.ships.filter(x => !! x.travel).forEach(renderShip)
            renderDestination(destination)

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
        context,
        update,
    }
}
