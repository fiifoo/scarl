import { combineReducers } from 'redux'
import create from './create'
import debug from './debug'
import game from './game'
import inventory from './inventory'
import main from './main'

export default combineReducers({
    create,
    debug,
    game,
    inventory,
    main,
})
