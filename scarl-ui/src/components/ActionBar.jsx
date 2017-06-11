import React from 'react'
import * as modes from '../game/modes'

const className = 'btn btn-default'
const activeClassName = 'btn btn-warning'
const getClassName = active => active ? activeClassName : className

const AimButton = ({mode, cancelMode, aim}) => {
    const active = mode === modes.AIM
    const onClick = active ? cancelMode : aim

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            disabled={mode !== modes.MAIN && mode !== modes.AIM}
            >Aim</button>
    )
}

const EnterConduitButton = ({mode, enterConduit}) => (
    <button
        className={className}
        onClick={enterConduit}
        disabled={mode !== modes.MAIN}
        >Use stairs</button>
)

const InventoryButton = ({mode, cancelMode, inventory}) => {
    const active = mode === modes.INVENTORY
    const onClick = active ? cancelMode : inventory

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            disabled={mode !== modes.MAIN && mode !== modes.INVENTORY}
            >Inventory</button>
    )
}

const KeyBindingsButton = ({mode, cancelMode, keyBindings}) => {
    const active = mode === modes.KEY_BINDINGS
    const onClick = active ? cancelMode : keyBindings

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            disabled={mode !== modes.MAIN && mode !== modes.KEY_BINDINGS}
            >Key bindings</button>
    )
}

const LookButton = ({mode, cancelMode, look}) => {
    const active = mode === modes.LOOK
    const onClick = active ? cancelMode : look

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            disabled={mode !== modes.MAIN && mode !== modes.LOOK}
            >Look</button>
    )
}

const MessageLogButton = ({mode, cancelMode, messageLog}) => {
    const active = mode === modes.MESSAGE_LOG
    const onClick = active ? cancelMode : messageLog

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            disabled={mode !== modes.MAIN && mode !== modes.MESSAGE_LOG}
            >Message log</button>
    )
}

const PickItemButton = ({mode, pickItem}) => (
    <button
        className={className}
        onClick={pickItem}
        disabled={mode !== modes.MAIN}
        >Pick item</button>
)

const TalkButton = ({mode, communicate}) => (
    <button
        className={className}
        onClick={communicate}
        disabled={mode !== modes.MAIN}
        >Talk</button>
)

const UseButton = ({mode, use}) => (
    <button
        className={className}
        onClick={use}
        disabled={mode !== modes.MAIN}
        >Use</button>
)

const UseDoorButton = ({mode, useDoor}) => (
    <button
        className={className}
        onClick={useDoor}
        disabled={mode !== modes.MAIN}
        >Use door</button>
)

const ActionBar = props =>  {
    const {mode, cancelMode} = props
    const {communicate, enterConduit, pickItem, use, useDoor} = props
    const {aim, inventory, keyBindings, look, messageLog} = props

    return (
        <div className="btn-toolbar">
            <AimButton
                mode={mode}
                cancelMode={cancelMode}
                aim={aim}
                />
            <LookButton
                mode={mode}
                cancelMode={cancelMode}
                look={look}
                />
            <PickItemButton
                mode={mode}
                pickItem={pickItem}
                />
            <EnterConduitButton
                mode={mode}
                enterConduit={enterConduit}
                />
            <TalkButton
                mode={mode}
                communicate={communicate}
                />
            <UseDoorButton
                mode={mode}
                useDoor={useDoor}
                />
            <UseButton
                mode={mode}
                use={use}
                />
            <InventoryButton
                mode={mode}
                cancelMode={cancelMode}
                inventory={inventory}
                />
            <MessageLogButton
                mode={mode}
                cancelMode={cancelMode}
                messageLog={messageLog} />
            <KeyBindingsButton
                mode={mode}
                cancelMode={cancelMode}
                keyBindings={keyBindings}
                />
        </div>
    )
}

export default ActionBar
