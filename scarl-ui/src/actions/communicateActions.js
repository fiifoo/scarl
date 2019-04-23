import * as types from './actionTypes'
import {  cancelMode } from './gameActions'

export const endCommunication = () => (dispatch, getState) => {
    const events = getState().ui.communication.events

    dispatch({
        type: types.END_COMMUNICATION,
    })

    if (events.size <= 1) {
        cancelMode()(dispatch)
    }
}
