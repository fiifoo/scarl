import { connect } from 'react-redux'
import { cancelMode, debugFov, debugWaypoint } from '../../actions/debugActions'
import DebugBar from './DebugBar.jsx'

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
