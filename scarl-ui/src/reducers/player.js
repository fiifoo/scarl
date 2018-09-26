import { List, Record } from 'immutable'
import * as types from '../actions/actionTypes'

// todo: move inventory & equipments here
const Player = Record({
    creature: undefined,
    equipmentStats: undefined,
    keys: [], // will be update constanly and won't do immutable Set conversion
    recipes: List(),
    recycledItems: List(),
})

export default (state = Player(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Player()
        }
        case types.RECEIVE_GAME_START:
        case types.RECEIVE_PLAYER_INVENTORY: {
            state = state.set('recipes', List(action.data.playerRecipes))
            state = state.set('recycledItems', List(action.data.recycledItems))

            return state
        }
        case types.RECEIVE_AREA_CHANGE: {
            return state.set('recycledItems', List())
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
