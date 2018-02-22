import Data from '../data/Data'
import * as api from '../api'
import { SUMMARY } from '../const/pages.js'
import { createItem, getItemReferences } from '../data/utils.js'
import * as types from './actionTypes'

export const changePage = page => (dispatch, getState) => {
    if (page === SUMMARY && getState().summary === null) {
        api.fetchSummary().then(summary => dispatch({
            type: types.RECEIVE_SUMMARY,
            summary,
        }))
    }

    dispatch ({
        type: types.CHANGE_PAGE,
        page,
    })
}

export const selectModel = model => ({
    type: types.SELECT_MODEL,
    model,
})

export const selectItem = item => ({
    type: types.SELECT_ITEM,
    item,
})

export const addItem = (model, id) => (dispatch, getState) => {
    const models = getState().models
    const item = createItem(model, id, models)

    dispatch({
        type: types.ADD_ITEM,
        model,
        id,
        item,
    })
}

export const deleteItem = (model, id) => (dispatch, getState) => {
    const {data, models} = getState()
    const references = getItemReferences(data, models)(model, id)

    return references.isEmpty() ? (
        dispatch({
            type: types.DELETE_ITEM,
            model,
            id,
        })
    ) : (
        dispatch({
            type: types.SHOW_ITEM_REFERENCES,
            model,
            id,
            references,
        })
    )
}

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
    api.save(Data.write(data)).then(() => dispatch({
        type: types.SAVED,
    }))

    dispatch({
        type: types.SAVE,
    })
}

export const showItemReferences = (model, id) => (dispatch, getState) => {
    const {data, models} = getState()
    const references = getItemReferences(data, models)(model, id)

    dispatch({
        type: types.SHOW_ITEM_REFERENCES,
        model,
        id,
        references,
    })
}

export const hideItemReferences = () => ({
    type: types.HIDE_ITEM_REFERENCES,
})

export const showSideForm = (model, fieldType, path) => ({
    type: types.SHOW_SIDE_FORM,
    model,
    fieldType,
    path,
})

export const hideSideForm = () => ({
    type: types.HIDE_SIDE_FORM,
})

export const setEditorBrush = brush => ({
    type: types.SET_EDITOR_BRUSH,
    brush,
})

export const setEditorLocation = (location, locations) => ({
    type: types.SET_EDITOR_LOCATION,
    location,
    locations,
})

export const selectSummaryCreature = creature => ({
    type: types.SELECT_SUMMARY_CREATURE,
    creature,
})
