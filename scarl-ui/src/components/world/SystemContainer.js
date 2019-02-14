import { connect } from 'react-redux'
import { clearTravel } from '../../actions/worldActions'
import System from './System.jsx'

const SystemContainer = connect(
    state => ({
        spaceships: state.spaceships,
        stellarBodies: state.stellarBodies,
        ui: state.ui.world,
        world: state.world,
    }), {
        clearTravel,
    }
)(System)

export default SystemContainer
