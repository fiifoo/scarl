import * as types from '../actions/actionTypes'

export default (state = false, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return false
        }
        case types.RECEIVE_MESSAGE: {
            return ! action.data.player
        }
        default: {
            return state
        }
    }
}
