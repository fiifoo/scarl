import { connect } from 'react-redux'
import { COMMUNICATE } from '../../game/modes'
import Communication from './Communication.jsx'

const CommunicationContainer = connect(
    state => ({
        visible: state.ui.game.mode === COMMUNICATE,

        ui: state.ui.communication,
    })
)(Communication)

export default CommunicationContainer
