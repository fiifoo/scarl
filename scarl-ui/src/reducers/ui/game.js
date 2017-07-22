import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import * as modes from '../../game/modes'

const initial = Record({
    cursor: null,
    mode: modes.MAIN,
    reticule: null,
    trajectory: [],
    target: null,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_GAME_OVER: {
            return changeMode(state, modes.GAME_OVER)
        }
        case types.CHANGE_GAME_MODE: {
            return changeMode(state, action.mode)
        }
        case types.SET_CURSOR: {
            return state.set('cursor', action.cursor)
        }
        case types.SET_RETICULE: {
            return state.set('reticule', action.reticule).set('trajectory', action.trajectory)
        }
        case types.SET_TARGET: {
            return state.set('target', action.target)
        }
        default: {
            return state
        }
    }
}

const changeMode = (state, mode) => {
    state = state.set('cursor', null).set('reticule', null).set('trajectory', [])

    return state.set('mode', mode)
}
