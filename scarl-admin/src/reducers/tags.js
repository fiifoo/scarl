import { Set } from 'immutable'
import * as types from '../actions/actionTypes'

export default (state = Set(), action) => {
    switch (action.type) {
        case types.ADD_TAG: {
            return state.add(action.tag)
        }
        default: {
            return state
        }
    }
}
