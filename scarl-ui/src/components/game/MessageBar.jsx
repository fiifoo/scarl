import React from 'react'

const MessageBar = ({messages}) => (
    <div>
        {messages.size > 0 ? messages.join(' ') : null} &nbsp;
    </div>
)

export default MessageBar
