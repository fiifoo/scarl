import { connect } from 'react-redux'
import Game from '../components/Game.jsx'

const GameContainer = connect(
    state => ({
        connection: state.connection,
        gameOver: state.gameOver,
    })
)(Game)

export default GameContainer
