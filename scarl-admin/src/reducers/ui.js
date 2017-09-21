import { Record } from 'immutable'
import * as types from '../actions/actionTypes'

const initial = Record({
    addItemId: null,
    item: null,
    model: null,
    saving: false,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.SELECT_MODEL: {
            return state.set('model', action.model).set('item', null)
        }
        case types.SELECT_ITEM: {
            return state.set('item', action.item)
        }
        case types.ADD_ITEM: {
            return state.set('item', action.id).set('addItemId', null)
        }
        case types.SET_ADD_ITEM_ID: {
            return state.set('addItemId', action.id)
        }
        case types.SAVE: {
            return state.set('saving', true)
        }
        case types.SAVED: {
            return state.set('saving', false)
        }
        default: {
            return state
        }
    }
}
