import React from 'react'
import MenuItem from '../MenuItem.jsx'

const ActionBar = props =>  {
    const {crafting, inventory, keyBindings, messageLog, playerInfo} = props

    return (
        <div>
            <MenuItem onClick={inventory} label="Inventory" />
            <MenuItem onClick={crafting} label="Crafting" />
            <MenuItem onClick={playerInfo} label="Player character" />
            <MenuItem onClick={messageLog} label="Message log" />
            <MenuItem onClick={keyBindings} label="Key bindings"/>
        </div>
    )
}

export default ActionBar
