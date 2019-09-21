import {  List, Record, Set } from 'immutable'
import * as types from '../../actions/actionTypes'
import * as pages from '../../const/pages'
import TabSet from '../../data/ui/TabSet'

const initial = Record({
    fetchingSummary: false,
    page: pages.MAIN,
    saving: false,
    simulating: false,
    tab: 1,
    tabs: List([1]),
    tabSets: List([TabSet({
        id: 1,
        tabs: Set([1])
    })]),
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
            const tabs = action.index !== undefined ? (
                state.tabs.insert(action.index, action.tab)
            ) : (
                state.tabs.push(action.tab)
            )

            const tabSetIndex = state.tabSets.findIndex(x => x.id === action.tabSetId)
            const tabSetTabs = state.tabSets.getIn([tabSetIndex, 'tabs']).add(action.tab)
            const tabSets = state.tabSets.setIn([tabSetIndex, 'tabs'], tabSetTabs)

            return state.set('tabs', tabs).set('tabSets', tabSets).set('tab', action.tab)
        }
        case types.DELETE_TAB: {
            const tabs = state.tabs.filter(x => x !== action.tab)

            const tabSets = state.tabSets.map(tabSet => (
                tabSet.tabs.contains(action.tab) ? (
                    tabSet.set('tabs', tabSet.tabs.delete(action.tab))
                ) : (
                    tabSet
                )
            ))

            return state.set('tabs', tabs).set('tabSets', tabSets).set('tab', action.nextTab)
        }
        case types.SORT_TABS: {
            return state.set('tabs', action.sorted)
        }
        case types.ADD_TAB_SET: {
            const tabSets = state.tabSets.unshift(action.tabSet)

            return state.set('tabSets', tabSets)
        }
        case types.DELETE_TAB_SET: {
            const tabs = state.tabs.filter(x => ! action.tabSet.tabs.contains(x))
            const tabSets = state.tabSets.filter(x => x.id !== action.tabSet.id)

            return state.set('tabs', tabs).set('tabSets', tabSets).set('tab', action.nextTab)
        }
        case types.RENAME_TAB_SET: {
            const key = state.tabSets.findKey(x => x.id === action.tabSet.id)

            return state.setIn(['tabSets', key, 'name'], action.name)
        }
        case types.TOGGLE_TAB_SET: {
            const key = state.tabSets.findKey(x => x.id === action.tabSet.id)

            return state.setIn(['tabSets', key, 'visible'], ! action.tabSet.visible)
        }
        case types.SORT_TAB_SETS: {
            return state.set('tabSets', action.sorted)
        }
        case types.FETCH_SUMMARY: {
            return state.set('fetchingSummary', true)
        }
        case types.RECEIVE_SUMMARY: {
            return state.set('fetchingSummary', false)
        }
        case types.SAVE: {
            return state.set('saving', true)
        }
        case types.SAVED: {
            return state.set('saving', false)
        }
        case types.SIMULATE: {
            return state.set('simulating', true)
        }
        case types.SIMULATED: {
            return state.set('simulating', false)
        }
        default: {
            return state
        }
    }
}
