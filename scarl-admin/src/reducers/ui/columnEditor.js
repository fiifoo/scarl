import { List, Record } from 'immutable'
import * as types from '../../actions/actionTypes'

const initial = Record({
    model: null,
    items: List(),
    properties: List(),
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.SET_COLUMN_EDITOR_MODEL: {
            return initial.set('model', action.model).set('items', action.items)
        }
        case types.SET_COLUMN_EDITOR_ITEMS: {
            return state.set('items', action.items)
        }
        case types.SET_COLUMN_EDITOR_PROPERTIES: {
            return state.set('properties', action.properties)
        }
        default: {
            return state
        }
    }
}
