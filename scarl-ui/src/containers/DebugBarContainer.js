import { connect } from 'react-redux'
import DebugBar from '../components/DebugBar.jsx'
import { cancelMode, debugFov, debugWaypoint } from '../actions/debugActions'

const DebugBarContainer = connect(
    state => ({
        mode: state.ui.debug.mode,
    }), {
        cancelMode,
        debugFov,
        debugWaypoint,
    }
)(DebugBar)

export default DebugBarContainer
