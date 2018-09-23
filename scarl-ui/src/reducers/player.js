import { Record, Set } from 'immutable'
import * as types from '../actions/actionTypes'

// todo: move inventory & equipments here
const Player = Record({
    creature: undefined,
    equipmentStats: undefined,
    keys: [], // will be update constanly and won't do immutable Set conversion
    recipes: Set(),
})

export default (state = Player(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Player()
        }
        case types.RECEIVE_GAME_START:
        case types.RECEIVE_PLAYER_INVENTORY: {
            return state.set('recipes', Set(action.data.playerRecipes))
        }
        case types.RECEIVE_GAME_UPDATE: {
            const data = action.data.player

            return state.withMutations(state => (
                state.set('creature', data.creature).set('equipmentStats', data.equipmentStats).set('keys', data.keys)
            ))
        }
        default: {
            return state
        }
    }
}
