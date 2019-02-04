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
import recipes from './recipes'
import settings from './settings'
import spaceships from './spaceships'
import statistics from './statistics'
import stellarBodies from './stellarBodies'
import ui from './ui'
import world from './world'

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
    recipes,
    settings,
    spaceships,
    statistics,
    stellarBodies,
    ui,
    world,
})
