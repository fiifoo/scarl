import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'

const initial = Record({
    mode: null,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CHANGE_DEBUG_MODE: {
            return state.set('mode', action.mode)
        }
        default: {
            return state
        }
    }
}
