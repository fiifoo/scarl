import { combineReducers } from 'redux'
import area from './area'
import connection from './connection'
import fov from './fov'
import gameOver from './gameOver'
import kinds from './kinds'
import messages from './messages'
import player from './player'
import statistics from './statistics'
import ui from './ui'

export default combineReducers({
    area,
    connection,
    fov,
    gameOver,
    kinds,
    messages,
    player,
    statistics,
    ui,
})
