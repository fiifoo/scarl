import { combineReducers } from 'redux'
import communication from './communication'
import crafting from './crafting'
import create from './create'
import debug from './debug'
import game from './game'
import inventory from './inventory'
import look from './look'
import main from './main'
import world from './world'

export default combineReducers({
    communication,
    crafting,
    create,
    debug,
    game,
    inventory,
    look,
    main,
    world,
})
