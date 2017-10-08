import { Record } from 'immutable'
import * as types from '../../actions/actionTypes'

const initial = Record({
    location: null,
    brush: Record({
        property: null,
        value: null,
    })(),
})()

export default (state = initial, action) => {
    switch (action.type) {
        case types.SELECT_MODEL:
        case types.SELECT_ITEM: {
            return initial
        }
        case types.SET_EDITOR_BRUSH: {
            return state.set('brush', action.brush)
        }
        case types.SET_EDITOR_LOCATION: {
            return state.set('location', action.location)
        }
        default: {
            return state
        }
    }
}
