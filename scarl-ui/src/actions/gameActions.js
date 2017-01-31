import * as types from './actionTypes'

import { sendMessage } from './connectionActions'

export const attack = target => dispatch => {
    sendMessage({
        type: 'Attack',
        data: {target: {value: target}},
    })(dispatch)

    dispatch({
        type: types.GAME_ATTACK,
        location,
    })
}

export const move = location => dispatch => {
    sendMessage({
        type: 'Move',
        data: {location},
    })(dispatch)

    dispatch({
        type: types.GAME_MOVE,
        location,
    })
}

export const pass = () => dispatch => {
    sendMessage({
        type: 'Pass',
        data: {},
    })(dispatch)

    dispatch({
        type: types.GAME_PASS,
    })
}
