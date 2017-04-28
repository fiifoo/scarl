import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import * as modes from '../../game/modes'

const initial = Record({
    cursor: null,
    inventoryVisible: false,
    mode: modes.MAIN,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CHANGE_GAME_MODE: {
            return state.set('mode', action.mode)
        }
        case types.SET_CURSOR_LOCATION: {
            return state.set('cursor', action.location)
        }
        case types.TOGGLE_INVENTORY: {
            return state.set('inventoryVisible', ! state.inventoryVisible)
        }
        default: {
            return state
        }
    }
}
