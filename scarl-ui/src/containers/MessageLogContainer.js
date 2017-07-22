import { connect } from 'react-redux'
import LazyToggleable from '../components/LazyToggleable.jsx'
import MessageLog from '../components/MessageLog.jsx'
import { GAME_OVER_SCREEN, MESSAGE_LOG } from '../game/modes'

const eventMessages = state => (
    state.events.all.filter(e => e.data.message !== undefined).map(e => e.data.message)
)

const MessageLogContainer = connect(
    state => ({
        component: MessageLog,
        visible: state.ui.game.mode === MESSAGE_LOG || state.ui.game.mode === GAME_OVER_SCREEN,

        messages: eventMessages(state),
    })
)(LazyToggleable)

export default MessageLogContainer
