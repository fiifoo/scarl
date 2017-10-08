import { Record } from 'immutable'

const Location = Record({
    x: null,
    y: null,
})
Location.add = (a, b) => Location({
    x: a.x + b.x,
    y: a.y + b.y,
})

export default Location
