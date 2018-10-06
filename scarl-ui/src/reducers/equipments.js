import { Map } from 'immutable'
import * as types from '../actions/actionTypes'

export default (state = Map(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Map()
        }
        case types.RECEIVE_GAME_START:
        case types.RECEIVE_AREA_CHANGE:
        case types.RECEIVE_PLAYER_INVENTORY: {
            return Map(action.data.equipments)
        }
        default: {
            return state
        }
    }
}
