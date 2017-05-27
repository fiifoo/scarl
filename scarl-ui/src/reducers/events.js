import { List } from 'immutable'
import * as types from '../actions/actionTypes'

const MAX = 100

const initial = {
    latest: List(),
    all: List(),
}

const createGenericEvent = message => ({
    type: 'GenericEvent',
    data: {message},
})

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.ADD_MESSAGE: {
            const event = createGenericEvent(action.message)

            return {
                latest: List([event]),
                all: state.all.push(event).take(MAX),
            }
        }
        case types.CHANGE_GAME_MODE: {
            return {
                latest: List(),
                all: state.all,
            }
        }
        case types.RECEIVE_GAME_UPDATE: {
            const events = List(action.data.events.reverse())

            return {
                latest: events,
                all: state.all.concat(events).take(MAX),
            }
        }
        default: {
            return state
        }
    }
}
