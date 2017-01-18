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
    connection = null
    dispatch(_closeConnection())
}

export const ping = () => dispatch => {
    connection.send({
        type: 'Pass',
        data: {},
    })
    dispatch(_ping())
    log('Sent')
}

export const move = location => dispatch => {
    if (! connection) {
        return
    }

    connection.send({
        type: 'Move',
        data: {location},
    })

    dispatch(_move(location))
    log('Moved')
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

const _move = location => ({
    type: types.MOVE,
    location,
})

const _receiveMessage = data => ({
    type: types.RECEIVE_MESSAGE,
    data,
})
