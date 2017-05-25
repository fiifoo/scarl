import { connect } from 'react-redux'
import LazyToggleable from '../components/LazyToggleable.jsx'
import MessageLog from '../components/MessageLog.jsx'
import { MESSAGE_LOG } from '../game/modes'

const MessageLogContainer = connect(
    state => ({
        component: MessageLog,
        visible: state.ui.game.mode === MESSAGE_LOG || state.game.over,

        messages: state.messages.all,
    })
)(LazyToggleable)

export default MessageLogContainer
