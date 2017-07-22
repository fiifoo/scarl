import React from 'react'

const MessageLog = ({messages}) => (
    <div>
        <h4>Latest messages:</h4>
        {messages.size === 0
            ? 'No messages'
            : messages.reverse().map((message, key) => <div key={key}>{message}</div>)
        }
    </div>
)

export default MessageLog
