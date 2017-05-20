import { Map } from 'immutable'
import * as types from '../actions/actionTypes'

export default (state = Map(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Map()
        }
        case types.RECEIVE_PLAYER_INVENTORY: {
            return Map(action.data.equipments.map(x => [x.key, x.value]))
        }
        default: {
            return state
        }
    }
}
