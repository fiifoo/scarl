import { combineReducers } from 'redux'
import connection from './connection'
import entities from './entities'
import ui from './ui'

export default combineReducers({
    connection,
    entities,
    ui,
})
