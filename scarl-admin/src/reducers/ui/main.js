import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import * as pages from '../../const/pages'

const initial = Record({
    addItemId: null,
    item: null,
    model: null,
    saving: false,
    sideForm: null,
    page: pages.MAIN,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CHANGE_PAGE: {
            return state.set('page', action.page)
        }
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
}