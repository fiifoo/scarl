import * as types from '../actions/actionTypes'

const initial = {
    running: false,
    over: false,
}

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_GAME_UPDATE: {
            if (! state.running) {
                return {
                    running: true,
                    over: false,
                }
            } else {
                return state
            }
        }
        case types.RECEIVE_GAME_OVER: {
            return {
                running: true,
                over: true,
            }
        }
        default: {
            return state
        }
    }
}
