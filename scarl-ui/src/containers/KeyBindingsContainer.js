import { connect } from 'react-redux'
import KeyBindings from '../components/KeyBindings.jsx'
import LazyToggleable from '../components/LazyToggleable.jsx'
import { KEY_BINDINGS } from '../game/modes'

const KeyBindingsContainer = connect(
    state => ({
        component: KeyBindings,
        visible: state.ui.game.mode === KEY_BINDINGS,
    })
)(LazyToggleable)

export default KeyBindingsContainer
