import { Map, Record, Set } from 'immutable'
import * as types from '../actions/actionTypes'

const Cost = Record({
    components: undefined,
    items: Set(),
})
Cost.read = ({items, ...values}) => Cost({
    ...values,
    items: Set(items),
})

const Recipe = Record({
    id: undefined,
    cost: undefined,
    item: undefined,
})
Recipe.read = ({cost, ...values}) => Recipe({
    ...values,
    cost: Cost.read(cost),
})

export default (state = Map(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Map()
        }
        case types.RECEIVE_GAME_START: {
            return Map(action.data.recipes.map(Recipe.read).map(x => [x.id, x]))
        }
        default: {
            return state
        }
    }
}
