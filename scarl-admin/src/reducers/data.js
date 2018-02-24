import * as types from '../actions/actionTypes'
import Data from '../data/Data'
import { writeItemId } from '../data/utils'

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
        case types.RENAME_ITEM: {
            const {model, id, newId, references} = action

            const path = model.dataPath.concat([id])
            const newPath = model.dataPath.concat([newId])
            const item = writeItemId(model, state.getIn(path), newId)

            return references.reduce((state, reference) => (
                state.setIn(reference, newId)
            ), state.deleteIn(path).setIn(newPath, item))
        }
        case types.SET_ITEM_VALUE: {
            return state.setIn(action.path, action.value)
        }
        default: {
            return state
        }
    }
}
