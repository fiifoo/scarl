import { connect } from 'react-redux'
import StatusBar from './StatusBar.jsx'

import { world as openWorld } from '../../actions/gameActions'

const StatusBarContainer = connect(
    state => ({
        equipmentSet: state.settings.equipmentSet,
        hasWorldActions: state.world.hasActions,
        player: state.player,
    }), {
        openWorld,
    }
)(StatusBar)

export default StatusBarContainer
