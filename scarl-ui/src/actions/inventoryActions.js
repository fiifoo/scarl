import * as types from './actionTypes'

export const setInventoryTab = tab => ({
    type: types.SET_INVENTORY_TAB,
    tab,
})

export const setInventoryRow = row => ({
    type: types.SET_INVENTORY_ROW,
    row,
})

export const setInventoryAction = action => ({
    type: types.SET_INVENTORY_ACTION,
    action,
})
