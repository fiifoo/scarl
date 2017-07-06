import WebSocket from '../utils/WebSocket'
import * as types from './actionTypes'
import { debugReceiveMessage } from './debugActions'

import { log } from '../utils/debug'

const receiveActionMap = {
    'DebugFov': types.RECEIVE_DEBUG_FOV,
    'DebugWaypoint': types.RECEIVE_DEBUG_WAYPOINT,
    'GameStart': types.RECEIVE_GAME_START,
    'GameUpdate': types.RECEIVE_GAME_UPDATE,
    'GameOver': types.RECEIVE_GAME_OVER,
    'AreaChange': types.RECEIVE_AREA_CHANGE,
    'PlayerInventory': types.RECEIVE_PLAYER_INVENTORY,
}

let connection = null

export const openConnection = () => (dispatch, getState) => {
    const onOpen = () => dispatch({
        type: types.CONNECTION_OPENED,
    })

    const onMessage = createMessageReceiver(dispatch, getState)

    const onClose = () => dispatch({
        type: types.CONNECTION_CLOSED,
    })

    connection = WebSocket('localhost:9000/ws', {
        onOpen,
        onMessage,
        onClose,
    })

    dispatch({
        type: types.OPEN_CONNECTION,
    })
}

export const closeConnection = () => dispatch => {
    connection.close()
    connection = null

    dispatch({
        type: types.CLOSE_CONNECTION,
    })
}

export const sendAction = (type, data = {}) => {
    sendMessage('GameAction', {
        action: {
            type,
            data
        }
    })
}

export const sendDebugFovQuery = () => {
    sendMessage('DebugFovQuery')
}

export const sendDebugWaypointQuery = () => {
    sendMessage('DebugWaypointQuery')
}

export const sendInventoryQuery = () => {
    sendMessage('InventoryQuery')
}

const sendMessage = (type, data = {}) => {
    log('Sending')
    connection.send({
        type,
        data,
    })
}

const createMessageReceiver = (dispatch, getState) => ({type, data}) => {
    if (! receiveActionMap[type]) {
        throw new Error(`Unknown message type ${type}.`)
    }

    log('Received')

    const mappedType = receiveActionMap[type]
    const debugging = getState().ui.debug.mode !== null

    if (debugging) {
        debugReceiveMessage(mappedType, data)(dispatch, getState)
    }

    dispatch({
        type: mappedType,
        data,
    })

    log('Done')
}
