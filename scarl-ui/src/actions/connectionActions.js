import * as types from './actionTypes'
import WebSocket from '../utils/WebSocket'

import { log } from '../utils/debug'

const receiveActionMap = {
    'GameStart': types.RECEIVE_GAME_START,
    'GameUpdate': types.RECEIVE_GAME_UPDATE,
    'GameOver': types.RECEIVE_GAME_OVER,
    'AreaChange': types.RECEIVE_AREA_CHANGE,
    'PlayerInventory': types.RECEIVE_PLAYER_INVENTORY,
}

let connection = null

export const openConnection = () => dispatch => {
    const onOpen = () => dispatch({
        type: types.CONNECTION_OPENED,
    })

    const onMessage = createMessageReceiver(dispatch)

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

const createMessageReceiver = dispatch => ({type, data}) => {
    if (! receiveActionMap[type]) {
        throw new Error(`Unknown message type ${type}.`)
    }

    log('Received')
    dispatch({
        type: receiveActionMap[type],
        data,
    })
    log('Done')
}
