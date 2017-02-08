import * as types from '../actions/actionTypes'

export default (state = null, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return null
        }
        case types.RECEIVE_MESSAGE: {
            return action.data.player ? action.data.player : null
        }
        default: {
            return state
        }
    }
}
