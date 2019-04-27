import * as types from './actionTypes'
import {  cancelMode, doAction } from './gameActions'

export const converse = (target, subject) => (dispatch, getState) => {
    doAction('Converse', {target, subject})(dispatch, getState)

    endCommunication()(dispatch, getState)
}

export const endCommunication = () => (dispatch, getState) => {
    const events = getState().ui.communication.events

    dispatch({
        type: types.END_COMMUNICATION,
    })

    if (events.size <= 1) {
        cancelMode()(dispatch)
    }
}

export const endConversation = () => (dispatch, getState) => {
    doAction('EndConversation', {})(dispatch, getState)

    endCommunication()(dispatch, getState)
}
