import { connect } from 'react-redux'
import ActionBar from '../components/ActionBar.jsx'
import { look, cancelMode } from '../actions/gameActions'
import { communicate } from '../actions/playerActions'
import { focusKeyboard } from '../actions/keyboard'

const ActionBarContainer = connect(
    state => ({
        mode: state.ui.game.mode,
    }), {
        cancelMode,
        communicate,
        focusKeyboard,
        look,
    }
)(ActionBar)

export default ActionBarContainer
