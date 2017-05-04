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
            >Aim</button>
    )
}

const LookButton = ({mode, cancelMode, look}) => {
    const active = mode === modes.LOOK
    const onClick = active ? cancelMode : look

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            >Look</button>
    )
}

const PickItemButton = ({pickItem}) => (
    <button
        className={className}
        onClick={pickItem}
        >Pick item</button>
)

const EnterConduitButton = ({enterConduit}) => (
    <button
        className={className}
        onClick={enterConduit}
        >Use stairs</button>
)

const TalkButton = ({communicate}) => (
    <button
        className={className}
        onClick={communicate}
        >Talk</button>
)

const InventoryButton = ({toggleInventory}) => (
    <button
        className={className}
        onClick={toggleInventory}
        >Inventory</button>
)

const MessageLogButton = ({toggleMessageLog}) => (
    <button
        className={className}
        onClick={toggleMessageLog}
        >Message log</button>
)

const KeyBindingsButton = ({toggleKeyBindings}) => (
    <button
        className={className}
        onClick={toggleKeyBindings}
        >Key bindings</button>
)

const ActionBar = props =>  {
    const {mode} = props
    const {cancelMode, focusKeyboard} = props
    const {aim, communicate, enterConduit, look, pickItem} = props
    const {toggleInventory, toggleKeyBindings, toggleMessageLog} = props

    const wrap = action => () => {
        action()
        focusKeyboard()
    }

    return (
        <div className="btn-toolbar">
            <AimButton mode={mode} cancelMode={wrap(cancelMode)} aim={wrap(aim)} />
            <LookButton mode={mode} cancelMode={wrap(cancelMode)} look={wrap(look)} />
            <PickItemButton pickItem={wrap(pickItem)} />
            <EnterConduitButton enterConduit={wrap(enterConduit)} />
            <TalkButton communicate={wrap(communicate)} />
            <InventoryButton toggleInventory={toggleInventory} />
            <MessageLogButton toggleMessageLog={toggleMessageLog} />
            <KeyBindingsButton toggleKeyBindings={toggleKeyBindings} />
        </div>
    )
}

export default ActionBar
