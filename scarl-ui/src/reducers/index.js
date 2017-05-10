import { combineReducers } from 'redux'
import area from './area'
import connection from './connection'
import factions from './factions'
import fov from './fov'
import game from './game'
import kinds from './kinds'
import map from './map'
import messages from './messages'
import player from './player'
import statistics from './statistics'
import ui from './ui'

export default combineReducers({
    area,
    connection,
    factions,
    fov,
    game,
    kinds,
    map,
    messages,
    player,
    statistics,
    ui,
})
