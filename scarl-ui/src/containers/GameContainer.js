import { connect } from 'react-redux'
import Game from '../components/Game.jsx'

const GameContainer = connect(
    state => ({
        game: state.game,
    })
)(Game)

export default GameContainer
