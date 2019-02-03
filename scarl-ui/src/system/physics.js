import * as coords from './coordinates'
import Vector from './Vector'

export const g = 9.80665

export const G = 6.67408 * Math.pow(10, -11)

export const AU = 149597871000

export const hypotenuse = (a, b) => Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2))

export const vector = (slope, length) => {
    const x = length / Math.sqrt(1 + Math.pow(slope, 2))
    const y = slope * x

    return Vector({x,y})
}

export const travelTime = (distance, acceleration) => Math.sqrt(2 * distance / acceleration)

export const gravity = (bodies, body) => bodies.reduce((result, other) => (
    other.id === body.id ? result : (
        Vector.add(result, calculateGravity(body, other))
    )
), Vector({x: 0, y: 0}))

const calculateGravity = (body1, body2) => {
    const quarter = coords.quarter(body1.position, body2.position)
    const p1 = coords.normalize(quarter, body1.position)
    const p2 = coords.normalize(quarter, body2.position)

    const m1 = body1.mass
    const m2 = body2.mass

    const dx = p2.x - p1.x
    const dy = p2.y - p1.y

    const r = hypotenuse(dx, dy)

    const F = G * m1 * m2 / Math.pow(r, 2)
    const a = F/m1

    return coords.normalize(quarter, vector(dy/dx, a))
}
