export const quarter = (a, b) => {
    const dx = b.x - a.x
    const dy = b.y - a.y

    if (dy == 0) {
        return dx >= 0 ? 0 : 2
    }
    if (dx == 0) {
        return dy >= 0 ? 1 : 3
    }

    return dy > 0 ? (
        dx > 0 ? 0 : 1
    ) : (
        dx < 0 ? 2 : 3
    )
}

export const normalize = (quarter, p) => {
    switch (quarter) {
        case 0: return p
        case 1: return {x: -p.x, y: p.y}
        case 2: return {x: -p.x, y: -p.y}
        case 3: return {x: p.x, y: -p.y}
    }
}
