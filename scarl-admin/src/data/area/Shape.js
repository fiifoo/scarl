export const calculateDimensions = shape => {
    const type = shape.get('type')
    const data = shape.get('data')

    switch (type) {
        case 'Rectangle': {
            const width = data.get('width')
            const height = data.get('height')
            const variance = data.get('variance')

            return {
                outerWidth: calculateMaxDimension(width, variance),
                outerHeight: calculateMaxDimension(height, variance),
                innerWidth: calculateMinDimension(width, variance),
                innerHeight: calculateMinDimension(height, variance),
            }
        }
        default: {
            throw new Error(`Unknown shape type ${type}.`)
        }
    }
}


const calculateMaxDimension = (value, variance) => value + calculateMaxVariance(value, variance)

const calculateMinDimension = (value, variance) => {
    const min = value - calculateMaxVariance(value, variance)

    return min < 1 ? 1 : min
}

const calculateMaxVariance = (value, variance) => Math.round(value * variance)
