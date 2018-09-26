import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import { COLUMN_RECIPES } from '../../game/crafting'
import * as modes from '../../game/modes'

const initial = Record({
    column: COLUMN_RECIPES,
    row: 0,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.CHANGE_GAME_MODE: {
            return action.mode !== modes.CRAFTING ? state : initial
        }
        case types.SET_CRAFTING_SELECTION: {
            return state.set('column', action.column).set('row', action.row)
        }
        default: {
            return state
        }
    }
}
