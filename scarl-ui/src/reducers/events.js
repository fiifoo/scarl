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
                all: state.all.push(event).takeLast(MAX),
            }
        }
        case types.CHANGE_GAME_MODE: {
            return {
                latest: List(),
                all: state.all,
            }
        }
        case types.RECEIVE_GAME_START:
        case types.RECEIVE_GAME_UPDATE: {
            const events = List(action.data.events)

            return {
                latest: events,
                all: state.all.concat(events).takeLast(MAX),
            }
        }
        case types.RECEIVE_GAME_OVER: {
            const event = createGenericEvent('<Press Enter to continue>')

            return {
                latest: state.latest.push(event),
                all: state.all,
            }
        }
        default: {
            return state
        }
    }
}
