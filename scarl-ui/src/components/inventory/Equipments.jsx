import React from 'react'
import { Table } from 'react-bootstrap'
import { DropButton, EquipButton, UnequipButton } from './buttons.jsx'

const EquipmentItem = ({equipped, group, item, kind, selected, dropItem, equipItem, unequipItem, select}) => {
    const equipment = item[group.prop]
    const stats = equipment.stats
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
            <td>{stats.melee.attack}</td>
            <td>{stats.melee.damage}</td>
            <td>{stats.ranged.attack}</td>
            <td>{stats.ranged.damage}</td>
            <td>{stats.ranged.range}</td>
            <td>{stats.defence}</td>
            <td>{stats.armor}</td>
            <td>{stats.sight.range}</td>
            <td>{stats.speed}</td>
        </tr>
    )
}

const Equipments = ({equipments, group, items, kinds, row, dropItem, equipItem, unequipItem, setRow}) => (
    <Table condensed className="inventory-group">
        <tbody>
            <tr>
                <td colSpan="3" className="full-width"><b>{group.label}</b></td>
                <td>Attack</td>
                <td>Damage</td>
                <td>Ranged attack</td>
                <td>Ranged damage</td>
                <td>Range</td>
                <td>Defence</td>
                <td>Armor</td>
                <td>Sight</td>
                <td>Speed</td>
            </tr>
            {items.toArray().map((item, index) =>
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
    </Table>
)

export default Equipments
