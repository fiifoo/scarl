import { connect } from 'react-redux'
import ActionBar from '../components/ActionBar.jsx'
import { aim, cancelMode, inventory, look, keyBindings, messageLog } from '../actions/gameActions'
import { communicate, enterConduit, pickItem, use, useDoor } from '../actions/playerActions'

const ActionBarContainer = connect(
    state => ({
        mode: state.ui.game.mode,
    }), {
        aim,
        cancelMode,
        communicate,
        enterConduit,
        inventory,
        keyBindings,
        look,
        messageLog,
        pickItem,
        use,
        useDoor: () => useDoor(), // receives bad argument from redux-thunk(?)
    }
)(ActionBar)

export default ActionBarContainer
