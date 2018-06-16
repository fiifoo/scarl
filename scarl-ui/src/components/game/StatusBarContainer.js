import { connect } from 'react-redux'
import StatusBar from './StatusBar.jsx'

const StatusBarContainer = connect(
    state => ({
        player: state.player,
    })
)(StatusBar)

export default StatusBarContainer
