import { connect } from 'react-redux'
import System from './System.jsx'

const SystemContainer = connect(
    state => ({
        spaceships: state.spaceships,
        stellarBodies: state.stellarBodies,
        world: state.world,
    })
)(System)

export default SystemContainer
