import { Map, Record } from 'immutable'
import * as types from '../actions/actionTypes'

const getTuple = x => [x.key, x.value]

const Statistics = Record({
    deaths: Map(),
})

export default (state = Statistics(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Statistics()
        }
        case types.RECEIVE_GAME_OVER: {
            return Statistics({
                deaths: Map(action.data.statistics.deaths.map(getTuple)),
            })
        }
        default: {
            return state
        }
    }
}
