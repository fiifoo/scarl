import { connect } from 'react-redux'
import StatusBar from './StatusBar.jsx'

const StatusBarContainer = connect(
    state => ({
        equipmentSet: state.settings.equipmentSet,
        player: state.player,
    })
)(StatusBar)

export default StatusBarContainer
