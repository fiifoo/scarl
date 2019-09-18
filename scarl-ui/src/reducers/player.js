import { fromJS, List, Map, Record, Set } from 'immutable'
import * as types from '../actions/actionTypes'
import { SIGNAL_MAP } from '../game/modes'

const Player = Record({
    conversation: undefined,
    creature: undefined,
    equipments: Map(),
    equipmentStats: undefined,
    inventory: Map(),
    keys: Set(),
    recipes: List(),
    recycledItems: List(),
    signals: null,
})

export default (state = Player(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Player()
        }
        case types.RECEIVE_GAME_UPDATE: {
            const data = action.data.player

            return state.withMutations(state => {
                state.set('conversation', data.conversation)
                state.set('creature', data.creature)

                if (data.equipments) {
                    state.set('equipments', Map(data.equipments))
                }
                if (data.equipmentStats) {
                    state.set('equipmentStats', data.equipmentStats)
                }
                if (data.inventory) {
                    state.set('inventory', Map(data.inventory.map(x => [x.id, x])))
                }
                if (data.keys) {
                    state.set('keys', Set(data.keys.map(key => fromJS(key))))
                }
                if (data.playerRecipes) {
                    state = state.set('recipes', List(data.playerRecipes))
                }
                if (data.recycledItems) {
                    state = state.set('recycledItems', List(data.recycledItems))
                }


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
