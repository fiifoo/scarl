import { connect } from 'react-redux'
import { blurKeyboard, keypress } from '../actions/keyboardActions'
import Keyboard from '../components/Keyboard.jsx'

const KeyboardContainer = connect(
    state => ({
        game: state.game,
        keyboardFocused: state.ui.keyboardFocused,
    }),
    {
        blurKeyboard,
        keypress
    }
)(Keyboard)

export default KeyboardContainer
