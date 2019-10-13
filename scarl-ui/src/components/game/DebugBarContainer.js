import { connect } from 'react-redux'
import { cancelMode, debugFov, debugParty, debugWaypoint } from '../../actions/debugActions'
import DebugBar from './DebugBar.jsx'

const DebugBarContainer = connect(
    state => ({
        mode: state.ui.debug.mode,
    }), {
        cancelMode,
        debugFov,
        debugParty,
        debugWaypoint,
    }
)(DebugBar)

export default DebugBarContainer
