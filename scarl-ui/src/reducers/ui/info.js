import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'

const initial = Record({
    messageLogVisible: false,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.TOGGLE_MESSAGE_LOG: {
            return state.set('messageLogVisible', ! state.messageLogVisible)
        }
        default: {
            return state
        }
    }
}
