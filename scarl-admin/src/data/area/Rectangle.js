import { Set } from 'immutable'
import Location from './Location'

export const calculateRectangle = (a, b) => {
    const x1 = Math.min(a.x, b.x)
    const x2 = Math.max(a.x, b.x)
    const y1 = Math.min(a.y, b.y)
    const y2 = Math.max(a.y, b.y)

    const locations = []
    for (let x = x1; x <= x2; x++) {
        for (let y = y1; y <= y2; y++) {
            locations.push(Location({x, y}))
        }
    }

    return Set(locations)
}
