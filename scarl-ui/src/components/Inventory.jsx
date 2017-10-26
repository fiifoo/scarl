import './Inventory.css'

import { Map } from 'immutable'
import React from 'react'
import { Button, Col, MenuItem, Row, SplitButton, Table } from 'react-bootstrap'
import { slots, groups } from '../game/equipment'

const createFilter = group => item => item[group] !== undefined
const createSorter = kinds => (a, b) => kinds.get(a.kind).name <= kinds.get(b.kind).name ? -1 : 1

const Empty = () => <i>Empty</i>

const DropButton = ({item, dropItem}) => (
    <Button
        bsSize="xsmall"
        onClick={() => dropItem(item.id)}>
        Drop
    </Button>
)

const EquipButton = ({item, allowedSlots, equipItem}) =>  {
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

const UnequipButton = ({item, unequipItem}) => (
    <Button
        bsSize="xsmall"
        onClick={() => unequipItem(item.id)}>
        Unequip
    </Button>
)

const UseButton = ({item, useItem}) => (
    <Button
        bsSize="xsmall"
        onClick={() => useItem(item.id)}>
        Use
    </Button>
)

const EquipmentItem = ({equipped, group, item, kind, dropItem, equipItem, unequipItem}) => {
    const equipment = item[group.prop]
    const stats = equipment.stats
    const allowedSlots = group.getSlots(equipment)

    return (
        <tr className={equipped ? 'active' : null}>
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
        </tr>
    )
}

const EquipmentGroup = ({equipments, group, items, kinds, dropItem, equipItem, unequipItem}) => (
    <tbody>
        <tr>
            <td colSpan="3"><b>{group.label}</b></td>
            <td>Attack</td>
            <td>Damage</td>
            <td>Ranged attack</td>
            <td>Ranged damage</td>
            <td>Range</td>
            <td>Defence</td>
            <td>Armor</td>
            <td>Sight</td>
        </tr>
        {items.map(item =>
            <EquipmentItem
                key={item.id}
                equipped={equipments.contains(item.id)}
                group={group}
                item={item}
                kind={kinds.get(item.kind)}
                dropItem={dropItem}
                equipItem={equipItem}
                unequipItem={unequipItem} />
        ).toArray()}
    </tbody>
)

const Equipments = ({equipments, inventory, kinds, dropItem, equipItem, unequipItem}) => {
    const sorter = createSorter(kinds)

    const renderGroup = group => (
        <EquipmentGroup
            key={group.prop}
            equipments={equipments}
            group={group}
            items={inventory.filter(createFilter(group.prop)).sort(sorter)}
            kinds={kinds}
            dropItem={dropItem}
            equipItem={equipItem}
            unequipItem={unequipItem} />
    )

    return (
        <Table condensed className="inventory-group">
            {Map(groups).map(renderGroup).toArray()}
        </Table>
    )
}

const UsableItem = ({item, kind, dropItem, useItem}) => (
    <tr>
        <td><UseButton item={item} useItem={useItem} /></td>
        <td><DropButton item={item} dropItem={dropItem} /></td>
        <td className="full-width">{kind.name}</td>
    </tr>
)

const Usables = ({inventory, kinds, dropItem, useItem}) => {
    const sorter = createSorter(kinds)
    const filter = createFilter('usable')
    const items = inventory.filter(filter).sort(sorter)

    return (
        <Table condensed className="inventory-group">
            <tbody>
                <tr>
                    <td colSpan="3"><b>Usables</b></td>
                </tr>
                {items.map(item =>
                    <UsableItem
                        key={item.id}
                        item={item}
                        kind={kinds.get(item.kind)}
                        dropItem={dropItem}
                        useItem={useItem} />
                ).toArray()}
            </tbody>
        </Table>
    )
}

const Equipped = ({equipments, inventory, kinds}) => {
    const renderEquipment = slot => {
        const item = equipments.get(slot.key)

        return (item
            ? kinds.get(inventory.get(item).kind).name
            : (<Empty />)
        )
    }

    const renderSlot = slot => (
        <tr key={slot.key}>
            <th className="text-right">{slot.label}</th>
            <td>
                {renderEquipment(slot)}
            </td>
        </tr>
    )

    return (
        <Table striped>
            <tbody>
                {Map(slots).map(renderSlot).toArray()}
            </tbody>
        </Table>
    )
}

const Inventory = ({equipments, inventory, kinds, dropItem, equipItem, unequipItem, useItem}) =>  (
    <Row>
        <Col xs={4}>
            <Equipped
                equipments={equipments}
                inventory={inventory}
                kinds={kinds.items} />
        </Col>
        <Col xs={8}>
            <Equipments
                equipments={equipments}
                inventory={inventory}
                kinds={kinds.items}
                dropItem={dropItem}
                equipItem={equipItem}
                unequipItem={unequipItem} />
            <Usables
                inventory={inventory}
                kinds={kinds.items}
                dropItem={dropItem}
                useItem={useItem} />
        </Col>
    </Row>
)

export default Inventory
