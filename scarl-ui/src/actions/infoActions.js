import * as types from './actionTypes'

export const addMessage = message => dispatch => dispatch({
    type: types.ADD_MESSAGE,
    message,
})

export const toggleKeyBindings = () => ({
    type: types.TOGGLE_KEY_BINDINGS,
})

export const toggleMessageLog = () => ({
    type: types.TOGGLE_MESSAGE_LOG,
})
