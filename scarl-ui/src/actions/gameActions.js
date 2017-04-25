import * as modes from '../game/modes'
import * as types from './actionTypes'

export const look = () => (dispatch, getState) => {
    const {player} = getState()

    setCursorLocation(player.creature.location)(dispatch)
    changeMode(modes.LOOK)(dispatch)
}

export const cancelMode = () => dispatch => {
    setCursorLocation(null)(dispatch)
    changeMode(modes.MAIN)(dispatch)
}

export const setCursorLocation = location => dispatch => dispatch({
    type: types.SET_CURSOR_LOCATION,
    location,
})

const changeMode = mode => dispatch => dispatch({
    type: types.CHANGE_GAME_MODE,
    mode,
})
