import { Map, Record } from 'immutable'
import * as types from '../actions/actionTypes'

const Faction = Record({
    id: undefined,
    dispositions: Map(),
})

const buildFaction = ({id, dispositions}) => Faction({
    id,
    dispositions: Map(dispositions),
})

export default (state = Map(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Map()
        }
        case types.RECEIVE_GAME_START: {
            return Map(action.data.factions.map(buildFaction).map(x => [x.id, x]))
        }
        default: {
            return state
        }
    }
}
