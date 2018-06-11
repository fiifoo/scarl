import React from 'react'
import { Button, MenuItem, SplitButton } from 'react-bootstrap'
import { slots as equipmentSlots } from '../../game/equipment'

export const DropButton = ({item, dropItem}) => (
    <Button
        bsSize="xsmall"
        onClick={() => dropItem(item.id)}>
        Drop
    </Button>
)

export const EquipButton = ({item, slots, fillAll, equipItem}) =>  {
    const defaultSlot = slots.get(0)
    const equipDefault = () => equipItem(item.id, defaultSlot)
    const id = `equip-${defaultSlot}-${item.id}`

    return !fillAll && slots.size > 1 ? (
        <SplitButton
            id={id}
            title="Equip"
            dropup
            bsSize="xsmall"
            onClick={equipDefault}>
            {slots.map(slot => (
                <MenuItem key={slot} onClick={() => equipItem(item.id, slot)}>
                    {equipmentSlots[slot].label}
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
