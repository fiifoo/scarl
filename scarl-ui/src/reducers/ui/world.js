import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'

const initial = Record({
    travel: null,
})()

export const TravelInfo = Record({
    to: undefined,
    possible: undefined,
    ship: undefined,
    travel: undefined,
    simulate: false,
})

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.SET_TRAVEL: {
            return state.set('travel', TravelInfo({
                to: action.to,
                possible: action.possible,
                ship: action.ship,
                travel: action.travel,
            }))
        }
        case types.SIMULATE_TRAVEL: {
            return state.setIn(['travel', 'simulate'], true)
        }
        case types.CLEAR_TRAVEL: {
            return state.set('travel', null)
        }
        default: {
            return state
        }
    }
}
