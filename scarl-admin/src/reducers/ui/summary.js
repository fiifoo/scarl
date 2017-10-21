import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'

const initial = Record({
    creature: null,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.SAVE: {
            return initial
        }
        case types.SELECT_SUMMARY_CREATURE: {
            return state.set('creature', action.creature)
        }
        default: {
            return state
        }
    }
}
