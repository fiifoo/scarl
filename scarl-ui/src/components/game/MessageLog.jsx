import React from 'react'
import { getEventMessages } from '../../game/utils'

const messageStyle = {
    whiteSpace: 'pre-wrap',
}

const MessageLog = ({events}) => {
    const messages = getEventMessages(events)

    return (
        <div>
            <h4>Latest messages:</h4>
            {messages.size === 0
                ? 'No messages'
                : messages.reverse().map((message, key) => <div key={key} style={messageStyle}>{message}</div>)
            }
        </div>
    )
}

export default MessageLog
