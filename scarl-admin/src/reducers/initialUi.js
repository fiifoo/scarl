import * as types from '../actions/actionTypes'

export default (state = null, action) => {
    switch (action.type) {
        case types.RECEIVE_SAVE_UI: {
            return action.initialUi
        }
        default: {
            return state
        }
    }
}
