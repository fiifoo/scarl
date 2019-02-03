import { Record } from 'immutable'

const Vector = Record({
    x: undefined,
    y: undefined,
})
Vector.add = (a, b) => Vector({
    x: a.x + b.x,
    y: a.y + b.y,
})
Vector.multiply = (vector, m) => Vector({
    x: vector.x * m,
    y: vector.y * m,
})

export default Vector
