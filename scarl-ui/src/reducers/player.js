import { List, Record } from 'immutable'
import * as types from '../actions/actionTypes'
import { SIGNAL_MAP } from '../game/modes'

// todo: move inventory & equipments here
const Player = Record({
    creature: undefined,
    equipmentStats: undefined,
    keys: [], // will be update constanly and won't do immutable Set conversion
    recipes: List(),
    recycledItems: List(),
    signals: null,
})

export default (state = Player(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Player()
        }
        case types.RECEIVE_GAME_START:
        case types.RECEIVE_AREA_CHANGE:
        case types.RECEIVE_PLAYER_INVENTORY: {
            state = state.set('recipes', List(action.data.playerRecipes))
            state = state.set('recycledItems', List(action.data.recycledItems))

            return state
        }
        case types.RECEIVE_GAME_UPDATE: {
            const data = action.data.player

            return state.withMutations(state => {
                state.set('creature', data.creature)
                state.set('equipmentStats', data.equipmentStats)
                state.set('keys', data.keys)
                if (action.mode !== SIGNAL_MAP) {
                    state.set('signals', null)
                }
            })
        }
        case types.RECEIVE_SIGNAL_MAP: {
            return state.set('signals', List(action.data.signals))
        }
        default: {
            return state
        }
    }
}
