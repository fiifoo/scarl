import { connect } from 'react-redux'
import { MESSAGE_LOG } from '../../game/modes'
import GameView from './GameView.jsx'
import MessageLog from './MessageLog.jsx'

const MessageLogContainer = connect(
    state => ({
        component: MessageLog,
        visible: state.ui.game.mode === MESSAGE_LOG,

        events: state.events.all,
    })
)(GameView)

export default MessageLogContainer
