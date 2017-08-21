import * as types from '../actions/actionTypes'

const initial = {
    created: false,
    running: false,
    over: false,
}

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED:
        case types.RECEIVE_CREATE_GAME_FAILED: {
            return initial
        }
        case types.CREATE_GAME: {
            return {
                ...state,
                created: true,
            }
        }
        case types.RECEIVE_GAME_UPDATE: {
            if (! state.running) {
                return {
                    ...state,
                    running: true,
                }
            } else {
                return state
            }
        }
        case types.RECEIVE_GAME_OVER: {
            return {
                ...state,
                over: true,
            }
        }
        default: {
            return state
        }
    }
}
