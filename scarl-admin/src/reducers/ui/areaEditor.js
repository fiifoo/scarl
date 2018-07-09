import { Record, Set } from 'immutable'
import * as types from '../../actions/actionTypes'
import Location from '../../data/area/Location'
import { tabbedReducer } from './utils'

const defaultLocation = Location({x: 0, y: 0})

const initial = Record({
    location: defaultLocation,
    locations: Set([defaultLocation]),
    brush: Record({
        property: null,
        value: null,
    })(),
})()

export default tabbedReducer(initial, (state, action) => {
    switch (action.type) {
        case types.SELECT_MODEL:
        case types.SELECT_ITEM: {
            return initial
        }
        case types.SET_AREA_EDITOR_BRUSH: {
            return state.set('brush', action.brush)
        }
        case types.SET_AREA_EDITOR_LOCATION: {
            return state.set('location', action.location).set('locations', action.locations)
        }
        default: {
            return state
        }
    }
})
