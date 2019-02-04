import { Record } from 'immutable'
import { gravity } from './physics'
import Position from './Position'
import Vector from './Vector'

const StellarBody = Record({
    id: undefined,
    source: undefined,
    mass: undefined,
    position: Position(),
    speed: Vector(),
})
StellarBody.read = ({position, speed, ...data}) => StellarBody({
    ...data,
    position: Position.read(position),
    speed: Vector.read(speed),
})
StellarBody.tick = (tick, bodies) => body => {
    const acceleration = gravity(bodies, body)

    const speed = Vector.add(body.speed, Vector.multiply(acceleration, tick))
    const position = Position.add(body.position, Vector.multiply(speed, tick))

    return body.set('speed', speed).set('position', position)
}

export default StellarBody
