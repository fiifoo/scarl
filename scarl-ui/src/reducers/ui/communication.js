import { List, Record } from 'immutable'
import * as types from '../../actions/actionTypes'

const initial = Record({
    events: List(),
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_GAME_UPDATE: {
            if (action.communicationEvents.length > 0) {
                return state.set('events', state.events.concat(action.communicationEvents))
            } else {
                return state
            }
        }
        case types.END_COMMUNICATION: {
            return state.set('events', state.events.rest())
        }
        default: {
            return state
        }
    }
}
