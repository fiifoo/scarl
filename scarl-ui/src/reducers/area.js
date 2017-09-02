import * as types from '../actions/actionTypes'

const initial = {
    id: null,
    map: [],
}

const buildMap = data => {
    const map = []

    data.forEach(item => {
        const x = item[0].x
        const y = item[0].y

        if (map[x] === undefined) {
            map[x] = []
        }
        map[x][y] = item[1]
    })

    return map
}

export default (state = null, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_GAME_START:
        case types.RECEIVE_AREA_CHANGE: {
            return {
                id: action.data.area,
                map: buildMap(action.data.map)
            }
        }
        default: {
            return state
        }
    }
}
