import { Record } from 'immutable'

const Position = Record({
    x: undefined,
    y: undefined,
})
Position.add = (position, vector) => Position({
    x: position.x + vector.x,
    y: position.y + vector.y,
})
Position.read = Position

export default Position
