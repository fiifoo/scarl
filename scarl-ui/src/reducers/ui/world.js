import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'
import { getCurrentStellarBody } from '../../game/world'
import { WorldInfo } from '../world'

import Position from '../../system/Position'

const SystemView = Record({
    center: Position({x: 0, y: 0}),
    scale: 1,
})

export const TravelInfo = Record({
    to: undefined,
    possible: undefined,
    ship: undefined,
    travel: undefined,
    simulate: false,
})

const initial = Record({
    systemView: SystemView(),
    travel: null,
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_GAME_START: {
            const body = getCurrentStellarBody(WorldInfo.read(action.data))

            return body ? state.setIn(['systemView', 'center'], body.position) : state
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
        case types.SET_SYSTEM_VIEW_CENTER: {
            return state.setIn(['systemView', 'center'], action.center)
        }
        case types.SET_SYSTEM_VIEW_SCALE: {
            return state.setIn(['systemView', 'scale'], action.scale)
        }
        default: {
            return state
        }
    }
}
