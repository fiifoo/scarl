import { connect } from 'react-redux'
import MessageLog from '../components/MessageLog.jsx'

const MessageLogContainer = connect(
    state => ({
        messages: state.messages.all,
    })
)(MessageLog)

export default MessageLogContainer
