import { isCommunicationEvent } from '../game/utils'
import WebSocket from '../utils/WebSocket'
import * as types from './actionTypes'
import { debugReceiveMessage } from './debugActions'

import { log } from '../utils/debug'

const receiveActionMappers = {
    Games: data => ({
        type: types.RECEIVE_GAMES,
        data,
    }),
    CreateGameFailed: data => ({
        type: types.RECEIVE_CREATE_GAME_FAILED,
        data,
    }),

    GameStart: data => ({
        type: types.RECEIVE_GAME_START,
        data,
        communicationEvents: data.events.filter(isCommunicationEvent),
    }),
    GameUpdate: (data, state) => ({
        type: types.RECEIVE_GAME_UPDATE,
        data,
        area: state.area,
        mode: state.ui.game.mode,
        communicationEvents: data.events.filter(isCommunicationEvent),
    }),
    GameOver: data => ({
        type: types.RECEIVE_GAME_OVER,
        data,
    }),
    AreaChange: data => ({
        type: types.RECEIVE_AREA_CHANGE,
        data,
    }),
    PlayerSettings: data => ({
        type: types.RECEIVE_PLAYER_SETTINGS,
        data,
    }),
    SignalMap: data => ({
        type: types.RECEIVE_SIGNAL_MAP,
        data,
    }),
    WorldInfo: data => ({
        type: types.RECEIVE_WORLD_INFO,
        data,
    }),

    DebugFov: data => ({
        type: types.RECEIVE_DEBUG_FOV,
        data,
    }),
    DebugWaypoint: data => ({
        type: types.RECEIVE_DEBUG_WAYPOINT,
        data,
    }),
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

export const sendWorldAction = (type, data = {}) => {
    sendMessage('WorldAction', {
        action: {
            type,
            data
        }
    })
}

export const sendAutoMove = (direction = undefined, destination = undefined) => {
    sendMessage('AutoMove', {
        direction,
        destination,
    })
}

export const sendDebugFovQuery = () => {
    sendMessage('DebugFovQuery')
}

export const sendDebugWaypointQuery = () => {
    sendMessage('DebugWaypointQuery')
}

export const sendSignalMapQuery = () => {
    sendMessage('SignalMapQuery')
}

export const sendSetEquipmentSet = set => {
    sendMessage('SetEquipmentSet', {set})
}

export const sendSetQuickItem = (slot, item) => {
    sendMessage('SetQuickItem', {slot, item})
}

export const sendCreateExistingGame = game => {
    sendMessage('CreateExistingGame', {game})
}

export const sendCreateNewGame = player => {
    sendMessage('CreateNewGame', {player})
}

const sendMessage = (type, data = {}) => {
    log('Sending')
    connection.send({
        type,
        data,
    })
}

const createMessageReceiver = (dispatch, getState) => messages => {
    log('Received')

    messages.forEach(({type, data}) => {
        if (! receiveActionMappers[type]) {
            throw new Error(`Unknown message type ${type}.`)
        }

        const state = getState()

        const debugging = state.ui.debug.mode !== null
        if (debugging) {
            debugReceiveMessage(type, data)(dispatch, getState)
        }

        const mapper = receiveActionMappers[type]
        dispatch(mapper(data, state))
    })

    log('Done')
}
