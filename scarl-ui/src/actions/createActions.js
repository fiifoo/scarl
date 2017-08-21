import * as types from './actionTypes'
import { sendCreateExistingGame, sendCreateNewGame } from './connectionActions'

export const createExistingGame = game => dispatch => {
    dispatch({
        type: types.CREATE_GAME,
    })
    sendCreateExistingGame(game)
}

export const createNewGame = () => (dispatch, getState) => {
    const player = getState().ui.create.player
    if (! player) {
        return
    }

    dispatch({
        type: types.CREATE_GAME,
    })
    sendCreateNewGame(player)
}

export const setNewGamePlayer = player => dispatch => {
    dispatch({
        type: types.SET_NEW_GAME_PLAYER,
        player,
    })
}
