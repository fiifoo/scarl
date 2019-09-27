import { connect } from 'react-redux'
import { STANCE } from '../../game/modes'
import GameView from './GameView.jsx'
import Stance from './Stance.jsx'

const StanceContainer = connect(
    state => ({
        component: Stance,
        layer: true,
        visible: state.ui.game.mode === STANCE,

        player: state.player,
    })
)(GameView)

export default StanceContainer
