import * as types from './actionTypes'
import WebSocket from '../utils/WebSocket'

import { log } from '../utils/debug'

let connection = null

export const openConnection = () => dispatch => {
    const onOpen = () => dispatch({
        type: types.CONNECTION_OPENED,
    })

    const onMessage = data => {
        log('Received')
        dispatch({
            type: types.RECEIVE_MESSAGE,
            data,
        })
        log('Done')
    }

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

export const sendMessage = data => dispatch => {
    log('Sending')
    connection.send(data)

    dispatch({
        type: types.SEND_MESSAGE,
        data,
    })
}
