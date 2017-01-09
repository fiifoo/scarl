import * as types from '../actions/actionTypes'

export default (state = false, action) => {
    switch (action.type) {
        case types.CONNECTION_OPENED: {
            return true
        }
        case types.CONNECTION_CLOSED: {
            return false
        }
        default: {
            return state
        }
    }
}
