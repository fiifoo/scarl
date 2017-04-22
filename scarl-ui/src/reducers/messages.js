import { List } from 'immutable'
import * as types from '../actions/actionTypes'

const MAX = 100

const initial = {
    latest: List(),
    all: List(),
}

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_MESSAGE: {
            const messages = List(action.data.messages.reverse())

            return {
                latest: messages,
                all: state.all.concat(messages).take(MAX)
            }
        }
        default: {
            return state
        }
    }
}
