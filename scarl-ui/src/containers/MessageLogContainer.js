import { connect } from 'react-redux'
import GameView from '../components/GameView.jsx'
import MessageLog from '../components/MessageLog.jsx'
import { MESSAGE_LOG } from '../game/modes'

const MessageLogContainer = connect(
    state => ({
        component: MessageLog,
        visible: state.ui.game.mode === MESSAGE_LOG,

        events: state.events.all,
    })
)(GameView)

export default MessageLogContainer
