import { connect } from 'react-redux'
import { storeGameViewSize } from '../actions/gameActions'
import GameViews from '../components/GameViews.jsx'

const GameViewsContainer = connect(
    state => ({
        size: state.ui.game.viewSize,
    }), {
        storeGameViewSize,
    }
)(GameViews)

export default GameViewsContainer
