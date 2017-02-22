import { connect } from 'react-redux'
import Statistics from '../components/Statistics.jsx'

const StatisticsContainer = connect(
    state => ({
        kinds: state.kinds,
        statistics: state.statistics,
    })
)(Statistics)

export default StatisticsContainer
