import * as types from '../actions/actionTypes'

const build = data => {
    const map = []

    data.forEach(item => {
        const x = item.key.x
        const y = item.key.y

        if (map[x] === undefined) {
            map[x] = []
        }
        map[x][y] = item.value
    })

    return map
}

const initial = {
    area: null,
    locations: [],
}

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_MESSAGE: {
            const area = action.data.area
            const data = action.data.map

            return state.area !== area || data ? ({
                area,
                locations: data ? build(data) : [],
            }) : state
        }
        default: {
            return state
        }
    }
}
