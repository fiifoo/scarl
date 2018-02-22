import * as types from '../actions/actionTypes'
import Data from '../data/Data'

export default (state = Data(), action) => {
    switch (action.type) {
        case types.ADD_ITEM: {
            const model = action.model
            const id = action.id
            const item = action.item
            const path = model.dataPath.concat([id])

            return state.setIn(path, item)
        }
        case types.DELETE_ITEM: {
            const model = action.model
            const id = action.id
            const path = model.dataPath.concat([id])

            return state.deleteIn(path)
        }
        case types.SET_ITEM_VALUE: {
            return state.setIn(action.path, action.value)
        }
        default: {
            return state
        }
    }
}
