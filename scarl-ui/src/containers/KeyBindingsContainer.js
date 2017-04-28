import { connect } from 'react-redux'
import { toggleKeyBindings } from '../actions/infoActions'
import { focusKeyboard } from '../actions/keyboard'
import KeyBindings from '../components/KeyBindings.jsx'
import LazyModal from '../components/LazyModal.jsx'

const KeyBindingsContainer = connect(
    state => ({
        component: KeyBindings,
        title: 'Key bindings',
        visible: state.ui.info.keyBindingsVisible,
    }), {
        focusKeyboard,
        toggle: toggleKeyBindings,
    }
)(LazyModal)

export default KeyBindingsContainer
