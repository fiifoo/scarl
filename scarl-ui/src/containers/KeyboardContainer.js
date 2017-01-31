import { connect } from 'react-redux'
import { blurKeyboard, keypress } from '../actions/keyboardActions'
import Keyboard from '../components/Keyboard.jsx'

const KeyboardContainer = connect(
    state => ({
        connection: state.connection,
        gameOver: state.gameOver,
        keyboardFocused: state.ui.keyboardFocused,
    }),
    {
        blurKeyboard,
        keypress
    }
)(Keyboard)

export default KeyboardContainer
