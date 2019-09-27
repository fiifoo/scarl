import { Set } from 'immutable'
import { connect } from 'react-redux'
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
    modes.COMMUNICATE,
    modes.GAME_OVER,
    modes.INTERACT,
    modes.LOOK,
    modes.MENU,
    modes.SIGNAL_MAP,
    modes.STANCE,
])

const preventMouseModes = Set([
    modes.COMMUNICATE,
])

const _autoMove = location => (dispatch, getState) => {
    const mode = getState().ui.game.mode

    if (! preventMouseModes.contains(mode)) {
        sendAutoMove(undefined, location)
        cancelMode()(dispatch)
    }
}

const _look = location => (dispatch, getState) => {
    const mode = getState().ui.game.mode

    if (! preventMouseModes.contains(mode)) {
        look(location)(dispatch, getState)
    }
}

const _focusKeyboard = () => dispatch => {
    dispatch(focusKeyboard())
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
    }), ({
        autoMove: _autoMove,
        look: _look,
        focusKeyboard: _focusKeyboard,
    })
)(GameView)

export default ScreenContainer
