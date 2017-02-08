import { combineReducers } from 'redux'
import connection from './connection'
import fov from './fov'
import gameOver from './gameOver'
import messages from './messages'
import player from './player'
import ui from './ui'

export default combineReducers({
    connection,
    fov,
    gameOver,
    messages,
    player,
    ui,
})
