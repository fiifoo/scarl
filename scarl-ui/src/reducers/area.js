import { Record } from 'immutable'
import * as types from '../actions/actionTypes'

const AreaInfo = Record({
    id: null,
    width: null,
    height: null,
    map: [],
})

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

export default (state = AreaInfo(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return AreaInfo()
        }
        case types.RECEIVE_GAME_START:
        case types.RECEIVE_AREA_CHANGE: {
            const area = action.data.area

            return AreaInfo({
                ...area,
                map: buildMap(area.map)
            })
        }
        default: {
            return state
        }
    }
}
