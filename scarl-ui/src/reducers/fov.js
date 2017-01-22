import { List } from 'immutable'
import * as types from '../actions/actionTypes'

export default (state = List(), action) => {
    switch (action.type) {
        case types.RECEIVE_MESSAGE: {
            return List(action.data.fov)
        }
        default: {
            return state
        }
    }
}
