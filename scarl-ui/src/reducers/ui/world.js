import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'

const initial = Record({
    travelSimulation: null,
})()

const TravelSimulation = Record({
    shipId: undefined,
    destinationId: undefined,
})

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.SET_TRAVEL_SIMULATION: {
            return state.set('travelSimulation', TravelSimulation({
                shipId: action.shipId,
                destinationId: action.destinationId,
            }))
        }
        case types.CLEAR_TRAVEL_SIMULATION: {
            return state.set('travelSimulation', null)
        }
        default: {
            return state
        }
    }
}
