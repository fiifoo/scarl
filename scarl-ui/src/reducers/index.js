import { combineReducers } from 'redux'
import connection from './connection'
import entities from './entities'
import fov from './fov'
import ui from './ui'

export default combineReducers({
    connection,
    entities,
    fov,
    ui,
})
