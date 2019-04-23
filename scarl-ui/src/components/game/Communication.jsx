import React from 'react'

const style = {
    maxWidth: 600,
    whiteSpace: 'pre-wrap',
    position: 'absolute',
    zIndex: 99,
    borderRight: '1px solid #777',
    borderBottom: '1px solid #777',
    borderTop: '1px dashed #777',
    backgroundColor: '#000',
    paddingRight: '5px',
    paddingBottom: '5px',
    paddingTop: '5px',
}

const Communication = props => {
    const { ui, visible } = props
    const event = ui.events.first()

    if (! visible || ! event) {
        return null
    }

    return (
        <div style={style}>
            {event.data.message}
        </div>
    )
}

export default Communication
