import { Map } from 'immutable'
import { combineReducers } from 'redux'
import * as types from '../actions/actionTypes'

const getTuple = entity => [entity.id, entity]

const factory = type => (state = Map(), action) => {
    switch (action.type) {
        case types.RECEIVE_MESSAGE: {
            const entities = action.data[type]

            return Map(entities.map(getTuple))
        }
        default: {
            return state
        }
    }
}

const creatures = factory('creatures')
const items = factory('items')

export default combineReducers({
    creatures,
    items,
})
