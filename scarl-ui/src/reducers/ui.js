import { Record } from 'immutable'
import * as types from '../actions/actionTypes'

const Ui = Record({
    closingConnection: false,
    openingConnection: false,
    keyboardFocused: false,
})

export default (state = Ui(), action) => {
    switch (action.type) {
        case types.OPEN_CONNECTION: {
            return state.set('openingConnection', true)
        }
        case types.CLOSE_CONNECTION: {
            return state.set('closingConnection', true)
        }
        case types.CONNECTION_OPENED: {
            return state.set('openingConnection', false).set('keyboardFocused', true)
        }
        case types.CONNECTION_CLOSED: {
            return state.set('openingConnection', false).set('closingConnection', false)
        }
        case types.FOCUS_KEYBOARD: {
            return state.set('keyboardFocused', true)
        }
        case types.BLUR_KEYBOARD: {
            return state.set('keyboardFocused', false)
        }
        default: {
            return state
        }
    }
}
