import { connect } from 'react-redux'
import LazyToggleable from '../components/LazyToggleable.jsx'
import MessageLog from '../components/MessageLog.jsx'
import { MESSAGE_LOG } from '../game/modes'

const eventMessages = state => (
    state.events.all.filter(e => e.data.message !== undefined).map(e => e.data.message)
)

const MessageLogContainer = connect(
    state => ({
        component: MessageLog,
        visible: state.ui.game.mode === MESSAGE_LOG || state.game.over,

        messages: eventMessages(state),
    })
)(LazyToggleable)

export default MessageLogContainer
