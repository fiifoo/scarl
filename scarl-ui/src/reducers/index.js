import { combineReducers } from 'redux'
import area from './area'
import connection from './connection'
import debug from './debug'
import equipments from './equipments'
import events from './events'
import factions from './factions'
import fov from './fov'
import game from './game'
import games from './games'
import inventory from './inventory'
import kinds from './kinds'
import player from './player'
import settings from './settings'
import statistics from './statistics'
import ui from './ui'

export default combineReducers({
    area,
    connection,
    debug,
    equipments,
    events,
    factions,
    fov,
    game,
    games,
    inventory,
    kinds,
    player,
    settings,
    statistics,
    ui,
})
