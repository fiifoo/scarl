import { combineReducers } from 'redux'
import debug from './debug'
import game from './game'
import main from './main'

export default combineReducers({
    debug,
    game,
    main,
})
