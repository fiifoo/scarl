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

export default (state = [], action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return []
        }
        case types.RECEIVE_MESSAGE: {
            return action.data.map ? build(action.data.map) : state
        }
        default: {
            return state
        }
    }
}
