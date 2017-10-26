import { connect } from 'react-redux'
import GameView from '../components/GameView.jsx'
import KeyBindings from '../components/KeyBindings.jsx'
import { KEY_BINDINGS } from '../game/modes'

const KeyBindingsContainer = connect(
    state => ({
        component: KeyBindings,
        visible: state.ui.game.mode === KEY_BINDINGS,
    })
)(GameView)

export default KeyBindingsContainer
