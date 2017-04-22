import { combineReducers } from 'redux'
import game from './game'
import info from './info'
import main from './main'

export default combineReducers({
    game,
    info,
    main,
})
