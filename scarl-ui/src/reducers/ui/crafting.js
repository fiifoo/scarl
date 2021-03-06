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
            return action.mode !== modes.CRAFTING ? state : initial.set('tab', state.tab)
        }
        case types.SET_CRAFTING_TAB: {
            return initial.set('tab', action.tab)
        }
        case types.SET_CRAFTING_ROW: {
            return state.set('row', action.row)
        }
        default: {
            return state
        }
    }
}
