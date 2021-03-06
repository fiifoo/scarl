import { List } from 'immutable'
import Data from '../data/Data'
import * as api from '../api'
import { SUMMARY } from '../const/pages.js'
import { copyItem, createItem, getItemReferences, isNewItemId } from '../data/utils.js'
import TabSet from '../data/ui/TabSet'
import { readUi, writeUi } from '../data/ui/utils'
import * as types from './actionTypes'

export const changePage = page => (dispatch, getState) => {
    if (page === SUMMARY && getState().summary === null) {
        dispatch ({
            type: types.FETCH_SUMMARY,
        })

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

export const addTab = tabSetId => (dispatch, getState) => {
    const tabs = getState().ui.main.tabs
    const tab = tabs.reduce((max, tab) => tab > max ? tab : max) + 1

    dispatch({
        type: types.ADD_TAB,
        tab,
        tabSetId,
    })
}

export const addAdjacentTab = () => (dispatch, getState) => {
    const ui = getState().ui.main

    const tab = ui.tabs.reduce((max, tab) => tab > max ? tab : max) + 1
    const tabSetId = ui.tabSets.find(x => x.tabs.contains(ui.tab)).id
    const index = ui.tabs.indexOf(ui.tab) + 1

    dispatch({
        type: types.ADD_TAB,
        tab,
        tabSetId,
        index,
    })
}

export const deleteTab = tab => (dispatch, getState) => {
    const ui = getState().ui.main

    let nextTab

    if (ui.tab !== tab) {
        nextTab = ui.tab
    } else {
        const index = ui.tabs.indexOf(ui.tab) + 1

        nextTab = ui.tabs.has(index) ? (
            ui.tabs.get(index)
        ) : (
            ui.tabs.find(t => t !== tab)
        )
    }

    dispatch({
        type: types.DELETE_TAB,
        tab,
        nextTab,
    })
}

export const sortTabs = ({oldIndex, newIndex}) => (dispatch, getState) => {
    const tabs = getState().ui.main.tabs
    const tab = tabs.get(oldIndex)
    const sorted = tabs.delete(oldIndex).insert(newIndex, tab)

    dispatch({
        type: types.SORT_TABS,
        sorted,
    })
}

export const addTabSet = () => (dispatch, getState) => {
    const tabSets = getState().ui.main.tabSets
    const tabSet = TabSet({
        id: tabSets.reduce((max, tabSet) => tabSet.id > max ? tabSet.id : max, 0) + 1,
    })

    dispatch({
        type: types.ADD_TAB_SET,
        tabSet,
    })
}

export const deleteTabSet = tabSet => (dispatch, getState) => {
    const ui = getState().ui.main

    const nextTab = tabSet.tabs.contains(ui.tab) ? ui.tabs.find(x => ! tabSet.tabs.contains(x)) : ui.tab

    dispatch({
        type: types.DELETE_TAB_SET,
        tabSet,
        nextTab,
    })
}

export const renameTabSet = (tabSet, name) => ({
    type: types.RENAME_TAB_SET,
    tabSet,
    name,
})

export const toggleTabSet = tabSet => ({
    type: types.TOGGLE_TAB_SET,
    tabSet,
})

export const sortTabSets = ({oldIndex, newIndex}) => (dispatch, getState) => {
    const tabSets = getState().ui.main.tabSets
    const tabSet = tabSets.get(oldIndex)
    const sorted = tabSets.delete(oldIndex).insert(newIndex, tabSet)

    dispatch({
        type: types.SORT_TAB_SETS,
        sorted,
    })
}

export const saveUi = () => (dispatch, getState) => {
    const ui = getState().ui

    const data = writeUi(ui)

    api.saveUi(data).then(() => {
        dispatch({
            type: types.RECEIVE_SAVE_UI,
            initialUi: readUi(data),
        })
    })
}

export const showItem = (model, item) => (dispatch, getState) => {
    addAdjacentTab()(dispatch, getState)
    dispatch(selectModel(model))
    dispatch(selectItem(item))
}

export const showReferenceItem = reference => (dispatch, getState) => {
    const {models} = getState()
    const separator = '|'

    const referenceString = reference.join(separator)
    const model = models.main.find(model => (
        referenceString.indexOf(model.dataPath.join(separator)) === 0
    ))
    const item = referenceString.replace(model.dataPath.join(separator) + separator, '').split(separator)[0]

    dispatch(hideItemReferences())
    showItem(model.id, item)(dispatch, getState)
}

export const save = () => (dispatch, getState) => {
    const data = getState().data
    api.save(Data.write(data)).then(() => dispatch({
        type: types.SAVED,
        hashCode: data.hashCode(),
    }))

    dispatch({
        type: types.SAVE,
    })
}

export const simulate = () => dispatch => {
    api.simulate().then(() => dispatch({
        type: types.SIMULATED,
    }))

    dispatch({
        type: types.SIMULATE,
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

export const addTag = tag => ({
    type: types.ADD_TAG,
    tag,
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

export const setAreaEditorVisible = visible => ({
    type: types.SET_AREA_EDITOR_VISIBLE,
    visible,
})

export const setAreaEditorBrush = brush => ({
    type: types.SET_AREA_EDITOR_BRUSH,
    brush,
})

export const setAreaEditorLocation = (location, locations) => ({
    type: types.SET_AREA_EDITOR_LOCATION,
    location,
    locations,
})

export const setColumnEditorModel = model => (dispatch, getState) => {
    const {data, models} = getState()
    const items = List(data.getIn(models.main.get(model).dataPath).keys())

    dispatch({
        type: types.SET_COLUMN_EDITOR_MODEL,
        model,
        items,
    })
}

export const setColumnEditorItems = items => ({
    type: types.SET_COLUMN_EDITOR_ITEMS,
    items,
})

export const setColumnEditorProperties = properties => ({
    type: types.SET_COLUMN_EDITOR_PROPERTIES,
    properties,
})

export const changeSummaryTab = tab => ({
    type: types.CHANGE_SUMMARY_TAB,
    tab,
})

export const selectSummaryCreature = creature => ({
    type: types.SELECT_SUMMARY_CREATURE,
    creature,
})
