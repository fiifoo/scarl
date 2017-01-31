import * as types from '../actions/actionTypes'

export default (state = null, action) => {
    switch (action.type) {
        case types.RECEIVE_MESSAGE: {
            return action.data.player
        }
        default: {
            return state
        }
    }
}
