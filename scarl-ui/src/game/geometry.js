export const distance = (from, to) => (
    Math.max(Math.abs(from.x - to.x), Math.abs(from.y - to.y))
)
