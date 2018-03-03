import Data from '../data/Data'
import * as api from '../api'
import { SUMMARY } from '../const/pages.js'
import { copyItem, createItem, getItemReferences, isNewItemId } from '../data/utils.js'
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

export const changeTab = tab => (dispatch, getState) => {
    const tabs = getState().ui.main.tabs

    if (tabs.includes(tab)) {
        dispatch({
            type: types.CHANGE_TAB,
            tab,
        })
    }
}

export const addTab = () => (dispatch, getState) => {
    const tabs = getState().ui.main.tabs
    const tab = tabs.last() + 1

    dispatch({
        type: types.ADD_TAB,
        tab,
    })
}

export const deleteTab = tab => (dispatch, getState) => {
    const ui = getState().ui.main
    const nextTab = ui.tab === tab ? ui.tabs.find(t => t !== tab) : ui.tab

    dispatch({
        type: types.DELETE_TAB,
        tab,
        nextTab,
    })
}

export const showItem = (model, item) => (dispatch, getState) => {
    addTab()(dispatch, getState)
    dispatch(selectModel(model))
    dispatch(selectItem(item))
}

export const showReferenceItem = reference => (dispatch, getState) => {
    const {models} = getState()

    const referenceString = reference.join('/')
    const model = models.main.find(model => (
        referenceString.indexOf(model.dataPath.join('/')) === 0
    ))
    const item = referenceString.replace(model.dataPath.join('/') + '/', '').split('/')[0]

    dispatch(hideItemReferences())
    showItem(model.id, item)(dispatch, getState)
}

export const save = () => (dispatch, getState) => {
    const data = getState().data
    api.save(Data.write(data)).then(() => dispatch({
        type: types.SAVED,
    }))

    dispatch({
        type: types.SAVE,
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

export const addItem = (model, id, copyId = undefined) => (dispatch, getState) => {
    const {data, models} = getState()

    if (isNewItemId(data, model, id)) {
        const item = copyId ? copyItem(model, copyId, id, data) : createItem(model, id, models)

        dispatch({
            type: types.ADD_ITEM,
            model,
            id,
            item,
        })
    } else {
        dispatch({
            type: types.SET_ITEM_ADD_INVALID,
        })
    }
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

export const renameItem = (model, id, newId) => (dispatch, getState) => {
    const {data, models} = getState()

    if (isNewItemId(data, model, newId)) {
        const references = getItemReferences(data, models)(model, id)

        dispatch({
            type: types.RENAME_ITEM,
            model,
            id,
            newId,
            references,
        })
    } else {
        dispatch({
            type: types.SET_ITEM_RENAME_INVALID,
        })
    }
}

export const setItemAddId = id => ({
    type: types.SET_ITEM_ADD_ID,
    id,
})

export const setItemRenameId = id => ({
    type: types.SET_ITEM_RENAME_ID,
    id,
})

export const setItemValue = (path, value) => ({
    type: types.SET_ITEM_VALUE,
    path,
    value,
})

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

export const changeSummaryTab = tab => ({
    type: types.CHANGE_SUMMARY_TAB,
    tab,
})

export const selectSummaryCreature = creature => ({
    type: types.SELECT_SUMMARY_CREATURE,
    creature,
})
