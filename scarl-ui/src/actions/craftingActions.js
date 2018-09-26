import * as types from './actionTypes'

export const setCraftingTab = tab => ({
    type: types.SET_CRAFTING_TAB,
    tab,
})

export const setCraftingRow = row => ({
    type: types.SET_CRAFTING_ROW,
    row,
})
