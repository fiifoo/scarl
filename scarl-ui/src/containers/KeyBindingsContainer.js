import { connect } from 'react-redux'
import { toggleKeyBindings } from '../actions/infoActions'
import { focusKeyboard } from '../actions/keyboard'
import KeyBindings from '../components/KeyBindings.jsx'

const KeyBindingsContainer = connect(
    state => ({
        visible: state.ui.info.keyBindingsVisible,
    }), {
        focusKeyboard,
        toggle: toggleKeyBindings,
    }
)(KeyBindings)

export default KeyBindingsContainer
