import { Map, Record } from 'immutable'
import * as types from '../actions/actionTypes'

const Kinds = Record({
    creatures: Map(),
    items: Map(),
    terrains: Map(),
    walls: Map(),
    widgets: Map(),
})

export default (state = Kinds(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return null
        }
        case types.RECEIVE_GAME_START: {
            return Kinds({
                creatures: Map(action.data.kinds.creatures),
                items: Map(action.data.kinds.items),
                terrains: Map(action.data.kinds.terrains),
                walls: Map(action.data.kinds.walls),
                widgets: Map(action.data.kinds.widgets),
            })
        }
        default: {
            return state
        }
    }
}
