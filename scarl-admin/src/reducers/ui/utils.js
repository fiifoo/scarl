import { Map, Record } from 'immutable'
import * as types from '../../actions/actionTypes'

export const tabState = state => state.tabs.get(state.tab)

export const tabbedReducer = (initial, reducer) => {
    const _initial = Record({
        tab: 1,
        tabs: Map().set(1, initial),
    })()

    return (state = _initial, action) => {
        switch (action.type) {
            case types.CHANGE_TAB: {
                return state.set('tab', action.tab)
            }
            case types.ADD_TAB: {
                const tabs = state.get('tabs').set(action.tab, reducer(initial, {
                    ...action,
                    previous: state.tabs.get(state.tab)
                }))

                return state.set('tabs', tabs).set('tab', action.tab)
            }
            case types.DELETE_TAB: {
                const tabs = state.tabs.delete(action.tab)

                return state.set('tabs', tabs).set('tab', action.nextTab)
            }
            default: {
                const current = state.tabs.get(state.tab)
                const next = reducer(current, action)

                return (current !== next) ? (
                    state.setIn(['tabs', state.tab], next)
                ) : (
                    state
                )
            }
        }
    }
}
