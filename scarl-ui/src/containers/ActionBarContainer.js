import { connect } from 'react-redux'
import ActionBar from '../components/ActionBar.jsx'
import { cancelMode, look, toggleInventory } from '../actions/gameActions'
import { toggleKeyBindings, toggleMessageLog } from '../actions/infoActions'
import { communicate, pickItem } from '../actions/playerActions'
import { focusKeyboard } from '../actions/keyboard'

const ActionBarContainer = connect(
    state => ({
        mode: state.ui.game.mode,
    }), {
        cancelMode,
        communicate,
        focusKeyboard,
        look,
        pickItem,
        toggleInventory,
        toggleKeyBindings,
        toggleMessageLog,
    }
)(ActionBar)

export default ActionBarContainer
