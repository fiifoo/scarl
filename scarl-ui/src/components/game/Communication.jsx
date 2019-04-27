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
    paddingRight: 5,
    paddingBottom: 5,
    paddingTop: 5,
}

const choiceStyle = {
    marginTop: 5,
}

const CommunicationChoice = ({choice, index, interrupted, event, converse}) =>  (
    <div style={choiceStyle}
        onClick={interrupted ? null : () => converse(event.data.source, choice.communication)}
        className={interrupted ? 'text-muted' : null}>
        <b>{index + 1}. </b>
        <span>{choice.description}</span>
    </div>
)

const Interrupted = () => (
    <div style={choiceStyle} className="text-danger"><i>Interrupted</i></div>
)

const Communication = ({conversation, ui, visible, converse}) => {
    const event = ui.events.first()

    if (! visible) {
        return null
    }

    const interrupted = event && event.data.choices.length > 0 && ! conversation

    return (
        <div style={style}>
            {event && event.data.message}
            {interrupted && <Interrupted />}
            {event && event.data.choices.map((choice, index) => (
                <CommunicationChoice
                    key={choice.communication}
                    choice={choice}
                    index={index}
                    interrupted={interrupted}
                    event={event}
                    converse={converse} />
            ))}
        </div>
    )
}

export default Communication
