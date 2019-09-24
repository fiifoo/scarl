import * as types from '../../actions/actionTypes'
import AreaEditor from '../../data/ui/AreaEditor'
import { tabbedReducer } from './utils'

const initial = AreaEditor()

export default tabbedReducer(initial, (state, action) => {
    switch (action.type) {
        case types.SELECT_MODEL:
        case types.SELECT_ITEM: {
            return initial
        }
        case types.SET_AREA_EDITOR_VISIBLE: {
            return state.set('visible', action.visible)
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
