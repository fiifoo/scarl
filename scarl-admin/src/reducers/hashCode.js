import * as types from '../actions/actionTypes'

export default (state = null, action) => {
    switch (action.type) {
        case types.SAVED: {
            return action.hashCode
        }
        default: {
            return state
        }
    }
}
