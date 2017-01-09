import * as types from './actionTypes'
import WebSocket from '../utils/WebSocket'

import { log } from '../utils/debug'

let connection = null

export const openConnection = () => dispatch => {
    const onOpen = () => dispatch(_connectionOpened())
    const onMessage = data => {
        log('Received')
        dispatch(_receiveMessage(data))
        log('Done')
    }
    const onClose = () => dispatch(_connectionClosed())

    connection = WebSocket('localhost:9000/ws', {
        onOpen,
        onMessage,
        onClose,
    })

    dispatch(_openConnection())
}

export const closeConnection = () => dispatch => {
    connection.close()
    dispatch(_closeConnection())
}

export const ping = () => dispatch => {
    connection.send({ping: 1})
    dispatch(_ping())
    log('Sent')
}

const _openConnection = () => ({
    type: types.OPEN_CONNECTION,
})

const _closeConnection = () => ({
    type: types.CLOSE_CONNECTION,
})

const _connectionOpened = () => ({
    type: types.CONNECTION_OPENED,
})

const _connectionClosed = () => ({
    type: types.CONNECTION_CLOSED,
})

const _ping = () => ({
    type: types.PING,
})

const _receiveMessage = data => ({
    type: types.RECEIVE_MESSAGE,
    data,
})
