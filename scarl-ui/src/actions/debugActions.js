import * as types from './actionTypes'
import { sendDebugFovQuery, sendDebugWaypointQuery } from './connectionActions'
import * as modes from '../debug/modes'

export const cancelMode = () => dispatch => changeMode(null)(dispatch)

export const debugReceiveMessage = type => (dispatch, getState) => {
    const mode = getState().ui.debug.mode

    switch (type) {
        case types.RECEIVE_GAME_UPDATE:
            if (mode === modes.FOV) {
                sendDebugFovQuery()
            }
            break
        default:
    }
}

export const debugFov = () => dispatch => {
    sendDebugFovQuery()
    changeMode(modes.FOV)(dispatch)
}

export const debugWaypoint = () => dispatch => {
    sendDebugWaypointQuery()
    changeMode(modes.WAYPOINT)(dispatch)
}

const changeMode = mode => dispatch => dispatch({
    type: types.CHANGE_DEBUG_MODE,
    mode,
})
