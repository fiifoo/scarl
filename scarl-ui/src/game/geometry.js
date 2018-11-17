import { Range } from 'immutable'

export const circle = (location, radius) => {
    const dimension = Range(0, radius * 2 + 1)
    const result = []

    dimension.forEach(x => {
        dimension.forEach(y => {
            result.push({
                x: x - radius + location.x,
                y: y - radius + location.y,
            })
        })
    })

    return result
}


export const distance = (from, to) => (
    Math.max(Math.abs(from.x - to.x), Math.abs(from.y - to.y))
)

export const line = (a, b) => {
    const oct = octant(a, b)
    const line = calculateLine(oct.normalize(a), oct.normalize(b))

    return line.map(oct.denormalize)
}

export const octant = (a, b) => {
    const value = calculateOctant(a, b)

    return {
        normalize: createOctantNormalizer(value),
        denormalize: createOctantDenormalizer(value),
    }
}

const calculateLine = (a, b) => {
    const dx = b.x - a.x
    const dy = b.y - a.y
    const de = dy / dx

    let error = 0
    let y = a.y
    const line = []

    // float comparison >= 0.5
    const errorLimit = 0.49999

    for (let x = a.x; x <= b.x; x++) {
        line.push({x, y})
        error = error + de
        if (error > errorLimit) {
            y = y + 1
            error = error - 1
        }
    }

    return line
}

const calculateOctant = (a, b) => {
    const dx = b.x - a.x
    const dy = b.y - a.y

    if (dy == 0) {
        return dx >= 0 ? 0 : 4
    }
    if (dx == 0) {
        return dy >= 0 ? 2 : 6
    }

    const s = Math.abs(dy / dx)

    return dy > 0 ? (
        dx > 0 ? (
            s < 1 ? 0 : 1
        ) : (
            s > 1 ? 2 : 3
        )
    ) : (
        dx < 0 ? (
            s < 1 ? 4 : 5
        ) : (
            s > 1 ? 6 : 7
        )
    )
}

const createOctantNormalizer = value => l => {
    switch (value) {
        case 0: return l
        case 1: return {x: l.y, y: l.x}
        case 2: return {x: l.y, y: -l.x}
        case 3: return {x: -l.x, y: l.y}
        case 4: return {x: -l.x, y: -l.y}
        case 5: return {x: -l.y, y: -l.x}
        case 6: return {x: -l.y, y: l.x}
        case 7: return {x: l.x, y: -l.y}
    }
}

const createOctantDenormalizer = value => l => {
    switch (value) {
        case 0: return l
        case 1: return {x: l.y, y: l.x}
        case 2: return {x: -l.y, y: l.x}
        case 3: return {x: -l.x, y: l.y}
        case 4: return {x: -l.x, y: -l.y}
        case 5: return {x: -l.y, y: -l.x}
        case 6: return {x: l.y, y: -l.x}
        case 7: return {x: l.x, y: -l.y}
    }
}
