import { combineReducers } from 'redux'
import connection from './connection'
import fov from './fov'
import gameOver from './gameOver'
import player from './player'
import ui from './ui'

export default combineReducers({
    connection,
    fov,
    gameOver,
    player,
    ui,
})
