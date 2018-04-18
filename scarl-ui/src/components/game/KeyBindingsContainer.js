import { connect } from 'react-redux'
import { KEY_BINDINGS } from '../../game/modes'
import GameView from './GameView.jsx'
import KeyBindings from './KeyBindings.jsx'

const KeyBindingsContainer = connect(
    state => ({
        component: KeyBindings,
        visible: state.ui.game.mode === KEY_BINDINGS,
    })
)(GameView)

export default KeyBindingsContainer
