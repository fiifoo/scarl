import * as types from '../actions/actionTypes'
import Summary from '../data/Summary'

export default (state = null, action) => {
    switch (action.type) {
        case types.SAVE: {
            return null
        }
        case types.RECEIVE_SUMMARY: {
            return Summary.read(action.summary)
        }
        default: {
            return state
        }
    }
}
