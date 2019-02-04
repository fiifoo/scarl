import { Map } from 'immutable'
import * as types from '../actions/actionTypes'

export default (state = Map(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Map()
        }
        case types.RECEIVE_GAME_START: {
            return Map(action.data.spaceships.map(x => [x.id, x]))
        }
        default: {
            return state
        }
    }
}
