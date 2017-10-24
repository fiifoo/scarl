import { connect } from 'react-redux'
import ActionBar from '../components/ActionBar.jsx'
import { cancelMode, inventory, look, keyBindings, messageLog } from '../actions/gameActions'

const ActionBarContainer = connect(
    state => ({
        mode: state.ui.game.mode,
    }), {
        cancelMode,
        inventory,
        keyBindings,
        look,
        messageLog,
    }
)(ActionBar)

export default ActionBarContainer
