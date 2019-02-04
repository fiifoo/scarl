import { Map, Record } from 'immutable'
import * as coords from './coordinates'
import { hypotenuse, travelTime } from './physics'
import Spaceship, { Travel } from './Spaceship'
import StellarBody from './StellarBody'

const TICK = 3600
const MAX_TRAVEL_TIME = 86400 * 100

const SolarSystem = Record({
    bodies: Map(),
    ships: Map(),
    time: undefined,
})
SolarSystem.read = data => SolarSystem({
    bodies: Map(data.bodies).map(StellarBody.read),
    ships: Map(data.ships).map(Spaceship.read),
    time: data.time,
})
SolarSystem.tick = (tick = TICK) => system => {
    const bodies = system.bodies.map(StellarBody.tick(tick, system.bodies))
    const ships = system.ships.map(Spaceship.tick(tick))

    return system.set('bodies', bodies).set('ships', ships).set('time', system.time + tick)
}
SolarSystem.calculateTravel = (shipId, destinationId, tick = TICK) => system => {
    const ship = system.ships.get(shipId)
    const port = system.bodies.get(ship.port)

    const to = calculateTravelTo(tick, system.bodies, ship, port.position, destinationId)

    return to ? (
        Travel({
            destination: destinationId,
            from: port.position,
            to,
            position: port.position,
        })
    ) : (
        null
    )
}

const calculateTravelTo = (tick, bodies, ship, from, destinationId) => {
    const check = checkTravelTo(ship, from)

    let destination = bodies.get(destinationId)
    let time = 0

    while (true) { // eslint-disable-line no-constant-condition
        if (time > MAX_TRAVEL_TIME) {
            return null
        }

        if (check(destination.position, time)) {
            return destination.position
        }

        bodies = bodies.map(StellarBody.tick(tick, bodies))
        destination = bodies.get(destination.id)
        time += TICK
    }
}

const checkTravelTo = (ship, from) => (to, time) => {
    const quarter = coords.quarter(from, to)
    const p1 = coords.normalize(quarter, from)
    const p2 = coords.normalize(quarter, to)

    const dx = p2.x - p1.x
    const dy = p2.y - p1.y

    const distance = hypotenuse(dx, dy)

    return travelTime(distance / 2, ship.acceleration) * 2 <= time
}


export default SolarSystem
