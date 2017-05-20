import { combineReducers } from 'redux'
import area from './area'
import connection from './connection'
import equipments from './equipments'
import factions from './factions'
import fov from './fov'
import game from './game'
import inventory from './inventory'
import kinds from './kinds'
import messages from './messages'
import player from './player'
import statistics from './statistics'
import ui from './ui'

export default combineReducers({
    area,
    connection,
    equipments,
    factions,
    fov,
    game,
    inventory,
    kinds,
    messages,
    player,
    statistics,
    ui,
})
