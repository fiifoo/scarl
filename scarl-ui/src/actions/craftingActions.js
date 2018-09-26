import * as types from './actionTypes'

export const setCraftingSelection = (column, row) => ({
    type: types.SET_CRAFTING_SELECTION,
    column,
    row,
})
