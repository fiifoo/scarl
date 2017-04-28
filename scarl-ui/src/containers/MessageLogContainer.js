import { connect } from 'react-redux'
import { toggleMessageLog } from '../actions/infoActions'
import { focusKeyboard } from '../actions/keyboard'
import LazyModal from '../components/LazyModal.jsx'
import MessageLog from '../components/MessageLog.jsx'

const MessageLogContainer = connect(
    state => ({
        component: MessageLog,
        title: 'Message log',
        visible: state.ui.info.messageLogVisible,

        messages: state.messages.all,
    }), {
        focusKeyboard,
        toggle: toggleMessageLog,
    }
)(LazyModal)

export default MessageLogContainer
