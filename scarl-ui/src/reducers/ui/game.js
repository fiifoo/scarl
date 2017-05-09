import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import * as modes from '../../game/modes'

const initial = Record({
    cursor: null,
    inventoryVisible: false,
    mode: modes.MAIN,
    reticule: null,
    trajectory: [],
    target: null,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CHANGE_GAME_MODE: {
            if (action.mode === modes.MAIN) {
                state = state.set('cursor', null).set('reticule', null).set('trajectory', [])
            }
            return state.set('mode', action.mode)
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
        case types.TOGGLE_INVENTORY: {
            return state.set('inventoryVisible', ! state.inventoryVisible)
        }
        default: {
            return state
        }
    }
}
