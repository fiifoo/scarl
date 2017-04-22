import React from 'react'
import { Modal } from 'react-bootstrap'

const MessageLog = ({messages, visible, focusKeyboard, toggle}) => (
    <Modal show={visible} onHide={toggle} onExited={focusKeyboard}>
        <Modal.Header closeButton>
            <Modal.Title>Message log</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            {messages.size === 0
                ? 'No messages'
                : messages.reverse().map(message => <div>{message}</div>)
            }
        </Modal.Body>
    </Modal>
)

export default MessageLog
