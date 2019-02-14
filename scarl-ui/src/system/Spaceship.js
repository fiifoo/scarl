import { Record } from 'immutable'
import * as coords from './coordinates'
import { hypotenuse, vector } from './physics'
import Position from './Position'
import Vector from './Vector'

const Travel = Record({
    destination: undefined,
    from: Position(),
    to: Position(),
    position: Position(),
    speed: Vector({x: 0, y: 0}),
    eta: undefined,
})
Travel.read = ({from, to, position, speed, ...data}) => Spaceship({
    ...data,
    from: Position.read(from),
    to: Position.read(to),
    position: Position.read(position),
    speed: Vector.read(speed),
})
Travel.distance = travel => hypotenuse(travel.to.x - travel.from.x, travel.to.y - travel.from.y)
Travel.finished = travel => {
    const quarter = coords.quarter(travel.from, travel.to)

    const p1 = coords.normalize(quarter, travel.position)
    const p2 = coords.normalize(quarter, travel.to)

    const dx = p2.x - p1.x

    // destination reached or speed in reverse
    return dx <= 0 || coords.normalize(quarter, travel.speed).x < 0
}

const Spaceship = Record({
    id: undefined,
    source: undefined,
    acceleration: undefined,
    travel: null,
    port: null,
})
Spaceship.read = ({travel, ...data}) => Spaceship({
    ...data,
    travel: travel ? Travel.read(travel) : null,
})
Spaceship.tick = tick => ship => {
    if (! ship.travel) {
        return ship
    }
    const acceleration = calculateTravelAcceleration(ship)

    const speed = Vector.add(ship.travel.speed, Vector.multiply(acceleration, tick))
    const position = Position.add(ship.travel.position, Vector.multiply(speed, tick))

    const next = ship.travel.set('position', position).set('speed', speed)

    return Travel.finished(next) ? (
        ship.set('travel', null).set('port', next.destination)
    ) : (
        ship.set('travel', next).set('port', null)
    )
}

const calculateTravelAcceleration = ship => {
    const quarter = coords.quarter(ship.travel.from, ship.travel.to)
    const p1 = coords.normalize(quarter, ship.travel.position)
    const p2 = coords.normalize(quarter, ship.travel.to)

    const dx = p2.x - p1.x
    const dy = p2.y - p1.y

    const distance = hypotenuse(dx, dy)
    const half = Travel.distance(ship.travel) / 2
    const a = dx < 0 || distance < half ? -ship.acceleration : ship.acceleration

    return coords.normalize(quarter, vector(dy/dx, a))
}

export { Travel }
export default Spaceship
