import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import { TEMPLATES } from '../../const/summaryTabs'

const initial = Record({
    tab: TEMPLATES,
    creature: null,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.SAVE:
        case types.SIMULATE: {
            return initial
        }
        case types.CHANGE_SUMMARY_TAB: {
            return state.set('tab', action.tab)
        }
        case types.SELECT_SUMMARY_CREATURE: {
            return state.set('creature', action.creature)
        }
        default: {
            return state
        }
    }
}
