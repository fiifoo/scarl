import { connect } from 'react-redux'
import { focusKeyboard } from '../actions/keyboard'
import Game from '../components/Game.jsx'

const GameContainer = connect(
    state => ({
        game: state.game,
    }), {
        focusKeyboard,
    }
)(Game)

export default GameContainer
