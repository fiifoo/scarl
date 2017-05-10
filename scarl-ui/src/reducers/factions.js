import { Map, Record, Set } from 'immutable'
import * as types from '../actions/actionTypes'

const Faction = Record({
    id: undefined,
    enemies: Set(),
})

const buildFaction = ({id, enemies}) => Faction({
    id,
    enemies: Set(enemies),
})

export default (state = Map(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Map()
        }
        case types.RECEIVE_MESSAGE: {
            if (action.data.factions) {
                return Map(action.data.factions.map(buildFaction).map(x => [x.id, x]))
            } else {
                return state
            }
        }
        default: {
            return state
        }
    }
}
