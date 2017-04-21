import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import * as modes from '../../game/modes'

const initial = Record({
    mode: modes.MAIN,
    cursor: null,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CHANGE_GAME_MODE: {
            return state.set('mode', action.mode)
        }
        case types.SET_CURSOR_LOCATION: {
            return state.set('cursor', action.location)
        }
        default: {
            return state
        }
    }
}
