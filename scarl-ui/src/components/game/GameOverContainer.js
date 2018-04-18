import { connect } from 'react-redux'
import { GAME_OVER_SCREEN } from '../../game/modes'
import GameView from './GameView.jsx'
import GameOver from './GameOver.jsx'

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
