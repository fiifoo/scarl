import { connect } from 'react-redux'
import { storeGameViewSize } from '../../actions/gameActions'
import GameViews from './GameViews.jsx'

const GameViewsContainer = connect(
    state => ({
        size: state.ui.game.viewSize,
    }), {
        storeGameViewSize,
    }
)(GameViews)

export default GameViewsContainer
