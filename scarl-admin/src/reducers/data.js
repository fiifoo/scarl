import * as types from '../actions/actionTypes'
import Data from '../data/Data'
import { createItem } from '../data/utils.js'

export default (state = Data(), action) => {
    switch (action.type) {
        case types.ADD_ITEM: {
            const model = action.model
            const id = action.id
            const path = model.dataPath.concat([id])
            const item = createItem(model, id)

            return state.setIn(path, item)
        }
        case types.SET_ITEM_VALUE: {
            return state.setIn(action.path, action.value)
        }
        default: {
            return state
        }
    }
}
