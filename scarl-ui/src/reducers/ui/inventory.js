import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import * as modes from '../../game/modes'

const initial = Record({
    tab: 0,
    row: 0,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.CHANGE_GAME_MODE: {
            return action.mode !== modes.INVENTORY ? state : state.set('row', 0)
        }
        case types.SET_INVENTORY_TAB: {
            return state.set('tab', action.tab).set('row', 0)
        }
        case types.SET_INVENTORY_ROW: {
            return state.set('row', action.row)
        }
        default: {
            return state
        }
    }
}
