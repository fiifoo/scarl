import { Map, Record } from 'immutable'
import * as types from '../actions/actionTypes'

const getTuple = kind => [kind.id, kind]

const buildBranch = kinds => Map(kinds.map(getTuple))

const Kinds = Record({
    creatures: Map(),
    items: Map(),
    terrains: Map(),
    walls: Map(),
})

export default (state = Kinds(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return null
        }
        case types.RECEIVE_MESSAGE: {
            if (action.data.kinds) {
                return Kinds({
                    creatures: buildBranch(action.data.kinds.creatures),
                    items: buildBranch(action.data.kinds.items),
                    terrains: buildBranch(action.data.kinds.terrains),
                    walls: buildBranch(action.data.kinds.walls),
                })
            } else {
                return state
            }
        }
        default: {
            return state
        }
    }
}
