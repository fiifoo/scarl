import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import * as modes from '../../game/modes'

const initial = Record({
    tab: 0,
    row: 0,
    action: null,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.CHANGE_GAME_MODE: {
            return action.mode !== modes.INVENTORY ? state : initial.set('tab', state.tab)
        }
        case types.RECEIVE_GAME_UPDATE: {
            return state.set('action', null)
        }
        case types.SET_INVENTORY_TAB: {
            return initial.set('tab', action.tab)
        }
        case types.SET_INVENTORY_ROW: {
            return state.set('row', action.row)
        }
        case types.SET_INVENTORY_ACTION: {
            return state.set('action', action.action)
        }
        default: {
            return state
        }
    }
}
