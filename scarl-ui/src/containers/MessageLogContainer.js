import { connect } from 'react-redux'
import { toggleMessageLog } from '../actions/infoActions'
import { focusKeyboard } from '../actions/keyboard'
import MessageLog from '../components/MessageLog.jsx'

const MessageLogContainer = connect(
    state => ({
        messages: state.messages.all,
        visible: state.ui.info.messageLogVisible,
    }), {
        focusKeyboard,
        toggle: toggleMessageLog,
    }
)(MessageLog)

export default MessageLogContainer
