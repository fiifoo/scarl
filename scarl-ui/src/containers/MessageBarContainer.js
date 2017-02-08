import { connect } from 'react-redux'
import MessageBar from '../components/MessageBar.jsx'

const MessageBarContainer = connect(
    state => ({
        messages: state.messages.latest,
    })
)(MessageBar)

export default MessageBarContainer
