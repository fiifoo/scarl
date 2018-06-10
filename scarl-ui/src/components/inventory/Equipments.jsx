import React from 'react'
import { DropButton, EquipButton, UnequipButton } from './buttons.jsx'

const EquipmentItem = ({equipped, group, item, kind, selected, dropItem, equipItem, unequipItem, select}) => {
    const allowedSlots = group.getSlots(item)

    return (
        <tr className={selected ? 'active' : null} onClick={select}>
            <td>
                {equipped ? (
                    <UnequipButton
                        item={item}
                        unequipItem={unequipItem} />
                ) : (
                    <EquipButton
                        item={item}
                        allowedSlots={allowedSlots}
                        equipItem={equipItem} />
                )}
            </td>
            <td><DropButton item={item} dropItem={dropItem} /></td>
            <td className="full-width">{kind.name}</td>
        </tr>
    )
}

const Equipments = ({equipments, group, items, kinds, row, dropItem, equipItem, unequipItem, setRow}) => (
    <table>
        <tbody>
            {items.map((item, index) =>
                <EquipmentItem
                    key={item.id}
                    equipped={equipments.contains(item.id)}
                    group={group}
                    item={item}
                    kind={kinds.get(item.kind)}
                    selected={index === row}
                    dropItem={dropItem}
                    equipItem={equipItem}
                    unequipItem={unequipItem}
                    select={() => setRow(index)} />
            )}
        </tbody>
    </table>
)

export default Equipments
