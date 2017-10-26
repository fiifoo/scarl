import React from 'react'

const getEventMessages = events => (
    events.filter(e => e.data.message !== undefined).map(e => e.data.message)
)

const MessageLog = ({events}) => {
    const messages = getEventMessages(events)

    return (
        <div>
            <h4>Latest messages:</h4>
            {messages.size === 0
                ? 'No messages'
                : messages.reverse().map((message, key) => <div key={key}>{message}</div>)
            }
        </div>
    )
}

export default MessageLog
