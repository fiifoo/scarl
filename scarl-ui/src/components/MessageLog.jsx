import React from 'react'

const MessageLog = ({messages}) => (
    <div>
        {messages.reverse().map(message => <div>{message}</div>)}
    </div>
)

export default MessageLog
