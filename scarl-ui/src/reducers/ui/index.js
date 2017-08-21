import { combineReducers } from 'redux'
import create from './create'
import debug from './debug'
import game from './game'
import main from './main'

export default combineReducers({
    create,
    debug,
    game,
    main,
})
