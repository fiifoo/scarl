import { connect } from 'react-redux'
import LazyToggleable from '../components/LazyToggleable.jsx'
import Statistics from '../components/Statistics.jsx'
import { GAME_OVER_SCREEN } from '../game/modes'

const StatisticsContainer = connect(
    state => ({
        component: Statistics,
        visible: state.ui.game.mode === GAME_OVER_SCREEN,

        kinds: state.kinds,
        statistics: state.statistics,
    })
)(LazyToggleable)

export default StatisticsContainer
