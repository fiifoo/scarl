import { connect } from 'react-redux'
import { converse } from '../../actions/communicateActions'
import { COMMUNICATE } from '../../game/modes'
import Communication from './Communication.jsx'

const CommunicationContainer = connect(
    state => ({
        visible: state.ui.game.mode === COMMUNICATE,

        conversation: state.player.conversation,
        ui: state.ui.communication,
    }), {
        converse,
    }
)(Communication)

export default CommunicationContainer
