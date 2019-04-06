import { List, Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import * as modes from '../../game/modes'
import { calculateScreenOffset, ScreenOffset } from '../../screen/utils'

const initial = Record({
    cursor: null,
    interaction: null,
    interactions: List(),
    missile: null,
    mode: modes.MAIN,
    reticule: null,
    screenOffset: ScreenOffset(),
    trajectory: [],
    target: null,
    viewSize: null,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_GAME_UPDATE: {
            return state.set('screenOffset', calculateScreenOffset(
                action.area,
                state.viewSize,
                state.screenOffset,
                action.data.player
            ))
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
        case types.SET_INTERACTION: {
            return state.set('interaction', action.interaction)
        }
        case types.SET_INTERACTIONS: {
            return state.set('interactions', action.interactions).set('interaction', 0)
        }
        case types.SET_MISSILE: {
            return state.set('missile', action.missile)
        }
        case types.SET_RETICULE: {
            return state.set('reticule', action.reticule).set('trajectory', action.trajectory)
        }
        case types.SET_TARGET: {
            return state.set('target', action.target)
        }
        case types.STORE_GAME_VIEW_SIZE: {
            return state.set('viewSize', action.size).set('screenOffset', calculateScreenOffset(
                action.area,
                action.size,
                state.screenOffset,
                action.player
            ))
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
