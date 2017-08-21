import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'

const initial = Record({
    player: null,
    error: false,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_CREATE_GAME_FAILED: {
            return state.set('error', true)
        }
        case types.SET_NEW_GAME_PLAYER: {
            return state.set('player', action.player)
        }
        default: {
            return state
        }
    }
}
