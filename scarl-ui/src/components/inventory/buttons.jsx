import React from 'react'
import { Button, MenuItem, SplitButton } from 'react-bootstrap'
import { slots } from '../../game/equipment'

export const DropButton = ({item, dropItem}) => (
    <Button
        bsSize="xsmall"
        onClick={() => dropItem(item.id)}>
        Drop
    </Button>
)

export const EquipButton = ({item, allowedSlots, equipItem}) =>  {
    const defaultSlot = allowedSlots.get(0)
    const equipDefault = () => equipItem(item.id, defaultSlot)
    const id = `equip-${defaultSlot}-${item.id}`

    return allowedSlots.size > 1 ? (
        <SplitButton
            id={id}
            title="Equip"
            dropup
            bsSize="xsmall"
            onClick={equipDefault}>
            {allowedSlots.map(slot => (
                <MenuItem key={slot} onClick={() => equipItem(item.id, slot)}>
                    {slots[slot].label}
                </MenuItem>
            )).toArray()}
        </SplitButton>
    ) : (
        <Button
            bsSize="xsmall"
            onClick={equipDefault}>
            Equip
        </Button>
    )
}

export const UnequipButton = ({item, unequipItem}) => (
    <Button
        bsSize="xsmall"
        bsStyle="primary"
        onClick={() => unequipItem(item.id)}>
        Unequip
    </Button>
)
