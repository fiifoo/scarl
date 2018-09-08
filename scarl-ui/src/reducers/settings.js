import * as types from '../actions/actionTypes'
import Settings from '../data/Settings'

export default (state = Settings(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Settings()
        }
        case types.RECEIVE_GAME_START:
        case types.RECEIVE_PLAYER_SETTINGS: {
            return Settings.read(action.data.settings)
        }
        default: {
            return state
        }
    }
}
