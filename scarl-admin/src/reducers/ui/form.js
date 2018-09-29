import { List, Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import { tabbedReducer } from './utils'

const ItemReferences = Record({
    references: List(),
    model: null,
    id: null,
})

const ItemAdd = Record({
    id: null,
    invalid: false,
})

const ItemRename = Record({
    id: null,
    invalid: false,
})

const initial = Record({
    item: null,
    itemAdd: ItemAdd(),
    itemReferences: null,
    itemRename: ItemRename(),
    model: null,
    sideForm: null,
})()

export default tabbedReducer(initial, (state, action) => {
    switch (action.type) {
        case types.SELECT_MODEL: {
            return state.set('model', action.model).set('item', null).set('itemRename', ItemRename())
        }
        case types.SELECT_ITEM: {
            return state.set('item', action.item).set('itemRename', ItemRename()).set('sideForm', null)
        }
        case types.ADD_ITEM: {
            return state.set('item', action.id).set('itemAdd', ItemAdd()).set('itemRename', ItemRename()).set('sideForm', null)
        }
        case types.DELETE_ITEM: {
            return state.set('item', null).set('itemRename', ItemRename())
        }
        case types.RENAME_ITEM: {
            return state.set('item', action.newId).set('itemRename', ItemRename())
        }
        case types.SET_ITEM_ADD_ID: {
            return state.set('itemAdd', ItemAdd({id: action.id}))
        }
        case types.SET_ITEM_ADD_INVALID: {
            return state.setIn(['itemAdd', 'invalid'], true)
        }
        case types.SET_ITEM_RENAME_ID: {
            return state.set('itemRename', ItemRename({id: action.id}))
        }
        case types.SET_ITEM_RENAME_INVALID: {
            return state.setIn(['itemRename', 'invalid'], true)
        }
        case types.SHOW_ITEM_REFERENCES: {
            return state.set('itemReferences', ItemReferences(action))
        }
        case types.HIDE_ITEM_REFERENCES: {
            return state.set('itemReferences', null)
        }
        case types.SHOW_SIDE_FORM: {
            return state.set('sideForm', {
                model: action.model,
                fieldType: action.fieldType,
                path: action.path,
            })
        }
        case types.HIDE_SIDE_FORM: {
            return state.set('sideForm', null)
        }
        default: {
            return state
        }
    }
})
