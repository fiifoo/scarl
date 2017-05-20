import * as types from '../actions/actionTypes'

export default (state = null, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return null
        }
        case types.RECEIVE_GAME_UPDATE: {
            return action.data.player
        }
        default: {
            return state
        }
    }
}
