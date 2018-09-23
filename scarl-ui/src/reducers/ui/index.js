import { combineReducers } from 'redux'
import crafting from './crafting'
import create from './create'
import debug from './debug'
import game from './game'
import inventory from './inventory'
import main from './main'

export default combineReducers({
    crafting,
    create,
    debug,
    game,
    inventory,
    main,
})
