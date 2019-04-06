import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'

const initial = Record({
    detailed: true,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED:
        case types.CHANGE_GAME_MODE: {
            return initial
        }
        case types.SET_LOOK_DETAILED: {
            return state.set('detailed', action.detailed)
        }
        default: {
            return state
        }
    }
}
