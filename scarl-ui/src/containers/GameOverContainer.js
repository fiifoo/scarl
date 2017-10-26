import { connect } from 'react-redux'
import GameView from '../components/GameView.jsx'
import GameOver from '../components/GameOver.jsx'
import { GAME_OVER_SCREEN } from '../game/modes'

const GameOverContainer = connect(
    state => ({
        component: GameOver,
        visible: state.ui.game.mode === GAME_OVER_SCREEN,

        events: state.events.all,
        kinds: state.kinds,
        statistics: state.statistics,
    })
)(GameView)

export default GameOverContainer
