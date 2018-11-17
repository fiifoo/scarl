import { Set } from 'immutable'
import { connect } from 'react-redux'
import { compose } from 'redux'
import { sendAutoMove } from '../../actions/connectionActions'
import { cancelMode, look } from '../../actions/gameActions'
import { focusKeyboard } from '../../actions/keyboard'
import * as modes from '../../game/modes'
import Screen from './Screen.jsx'
import GameView from './GameView.jsx'

const validModes = Set([
    modes.MAIN,
    modes.AIM,
    modes.AIM_MISSILE,
    modes.AUTO_MOVE,
    modes.GAME_OVER,
    modes.INTERACT,
    modes.LOOK,
    modes.MENU,
    modes.SIGNAL_MAP,
])

const autoMove = dispatch => location => {
    sendAutoMove(undefined, location)
    cancelMode()(dispatch)
}

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
        signals: state.ui.game.mode === modes.SIGNAL_MAP ? state.player.signals || [] : null,
        trajectory: state.ui.game.trajectory,
    }), dispatch => ({
        autoMove: autoMove(dispatch),
        look: compose(dispatch, look),
        focusKeyboard: compose(dispatch, focusKeyboard),
    })
)(GameView)

export default ScreenContainer
