import * as types from '../actions/actionTypes'

export default (state = [], action) => {
    switch (action.type) {
        case types.RECEIVE_CREATE_GAME_FAILED:
        case types.RECEIVE_GAMES: {
            return action.data.games
        }
        default: {
            return state
        }
    }
}
