import { connect } from 'react-redux'
import { blurKeyboard, keypress } from '../../actions/keyboard'
import Keyboard from './Keyboard.jsx'

const KeyboardContainer = connect(
    state => ({
        game: state.game,
        keyboardFocused: state.ui.main.keyboardFocused,
    }),
    {
        blurKeyboard,
        keypress
    }
)(Keyboard)

export default KeyboardContainer
