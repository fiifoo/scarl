import { Set } from 'immutable'
import { connect } from 'react-redux'
import * as modes from '../../game/modes'
import Screen from './Screen.jsx'
import GameView from './GameView.jsx'

const validModes = Set([modes.MAIN, modes.AIM, modes.AIM_MISSILE, modes.GAME_OVER, modes.INTERACT, modes.LOOK, modes.MENU])

const ScreenContainer = connect(
    state => ({
        component: Screen,
        lazy: false,
        scrollable: false,
        visible: validModes.contains(state.ui.game.mode),

        area: state.area,
        cursor: state.ui.game.cursor,
        debug: state.debug,
        debugMode: state.ui.debug.mode,
        events: state.events.latest,
        fov: state.fov,
        game: state.game,
        kinds: state.kinds,
        offset: state.ui.game.screenOffset,
        player: state.player,
        reticule: state.ui.game.reticule,
        trajectory: state.ui.game.trajectory,
    })
)(GameView)

export default ScreenContainer
