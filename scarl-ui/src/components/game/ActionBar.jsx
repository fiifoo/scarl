import React from 'react'

const Button = ({label, onClick}) =>  (
    <button
        className="btn btn-default"
        onClick={onClick}>
        {label}
    </button>
)

const ActionBar = props =>  {
    const {inventory, keyBindings, messageLog, playerInfo} = props

    return (
        <div className="btn-toolbar">
            <Button onClick={keyBindings} label="Key bindings"/>
            <Button onClick={inventory} label="Inventory" />
            <Button onClick={messageLog} label="Message log" />
            <Button onClick={playerInfo} label="Player character" />
        </div>
    )
}

export default ActionBar
