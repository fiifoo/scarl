import {  Record, OrderedSet } from 'immutable'
import * as types from '../../actions/actionTypes'
import * as pages from '../../const/pages'

const initial = Record({
    page: pages.MAIN,
    saving: false,
    tab: 1,
    tabs: OrderedSet([1]),
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CHANGE_PAGE: {
            return state.set('page', action.page)
        }
        case types.CHANGE_TAB: {
            return state.set('tab', action.tab)
        }
        case types.ADD_TAB: {
            const tabs = state.get('tabs').add(action.tab)

            return state.set('tabs', tabs).set('tab', action.tab)
        }
        case types.DELETE_TAB: {
            const tabs = state.tabs.delete(action.tab)

            return state.set('tabs', tabs).set('tab', action.nextTab)
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
