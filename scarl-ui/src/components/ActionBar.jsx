import React from 'react'
import * as modes from '../game/modes'

const className = 'btn btn-default'
const activeClassName = 'btn btn-warning'
const getClassName = active => active ? activeClassName : className

const InventoryButton = ({mode, cancelMode, inventory}) => {
    const active = mode === modes.INVENTORY
    const onClick = active ? cancelMode : inventory

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            disabled={mode !== modes.MAIN && mode !== modes.INVENTORY}>
            Inventory
        </button>
    )
}

const KeyBindingsButton = ({mode, cancelMode, keyBindings}) => {
    const active = mode === modes.KEY_BINDINGS
    const onClick = active ? cancelMode : keyBindings

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            disabled={mode !== modes.MAIN && mode !== modes.KEY_BINDINGS}>
            Key bindings
        </button>
    )
}

const LookButton = ({mode, cancelMode, look}) => {
    const active = mode === modes.LOOK
    const onClick = active ? cancelMode : look

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            disabled={mode !== modes.MAIN && mode !== modes.LOOK}>
            Look
        </button>
    )
}

const MessageLogButton = ({mode, cancelMode, messageLog}) => {
    const active = mode === modes.MESSAGE_LOG
    const onClick = active ? cancelMode : messageLog

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            disabled={mode !== modes.MAIN && mode !== modes.MESSAGE_LOG}>
            Message log
        </button>
    )
}

const ActionBar = props =>  {
    const {mode, cancelMode} = props
    const {inventory, keyBindings, look, messageLog} = props

    return (
        <div className="btn-toolbar">
            <LookButton
                mode={mode}
                cancelMode={cancelMode}
                look={look} />
            <InventoryButton
                mode={mode}
                cancelMode={cancelMode}
                inventory={inventory} />
            <MessageLogButton
                mode={mode}
                cancelMode={cancelMode}
                messageLog={messageLog} />
            <KeyBindingsButton
                mode={mode}
                cancelMode={cancelMode}
                keyBindings={keyBindings} />
        </div>
    )
}

export default ActionBar
