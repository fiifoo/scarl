import { Set } from 'immutable'
import { connect } from 'react-redux'
import Screen from '../components/Screen.jsx'
import Toggleable from '../components/Toggleable.jsx'
import * as modes from '../game/modes'

const validModes = Set([modes.MAIN, modes.AIM, modes.LOOK])

const ScreenContainer = connect(
    state => ({
        component: Screen,
        visible: validModes.contains(state.ui.game.mode),

        area: state.area,
        cursor: state.ui.game.cursor,
        fov: state.fov,
        game: state.game,
        kinds: state.kinds,
        reticule: state.ui.game.reticule,
        trajectory: state.ui.game.trajectory,
    })
)(Toggleable)

export default ScreenContainer
