import { connect } from 'react-redux'
import { cancelMode, inventory, look, keyBindings, messageLog } from '../../actions/gameActions'
import ActionBar from './ActionBar.jsx'

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
