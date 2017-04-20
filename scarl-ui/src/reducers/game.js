import * as types from '../actions/actionTypes'

const initial = {
    started: false,
    over: false,
}

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_MESSAGE: {
            if (! state.started) {
                return {
                    ...initial,
                    started: true,
                }
            } else if (! action.data.player) {
                return {
                    ...state,
                    over: true,
                }
            } else {
                return state
            }
        }
        default: {
            return state
        }
    }
}
