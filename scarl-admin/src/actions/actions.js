import Data from '../data/Data'
import * as api from '../api'
import * as types from './actionTypes'

export const selectModel = model => ({
    type: types.SELECT_MODEL,
    model,
})

export const selectItem = item => ({
    type: types.SELECT_ITEM,
    item,
})

export const addItem = (model, id) => ({
    type: types.ADD_ITEM,
    model,
    id,
})

export const setAddItemId = id => ({
    type: types.SET_ADD_ITEM_ID,
    id,
})

export const setItemValue = (path, value) => ({
    type: types.SET_ITEM_VALUE,
    path,
    value,
})

export const save = () => (dispatch, getState) => {
    const data = getState().data
    api.save(Data.write(data))

    setTimeout(() => dispatch({
        type: types.SAVED,
    }), 1000)

    dispatch({
        type: types.SAVE,
    })
}
