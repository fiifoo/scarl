import { Map } from 'immutable'
import React from 'react'
import { Button, Col, MenuItem, Panel, Row, SplitButton, Table } from 'react-bootstrap'
import { slots, groups } from '../game/equipment'

const createFilter = group => item => item[group] !== undefined
const createSorter = kinds => (a, b) => kinds.get(a.kind).name <= kinds.get(b.kind).name ? -1 : 1

const Empty = () => <i className="text-muted">Empty</i>

const EquipButton = ({item, allowedSlots, equipItem}) =>  {
    const defaultSlot = allowedSlots.get(0)
    const equipDefault = () => equipItem(item.id, defaultSlot)
    const id = `equip-${defaultSlot}-${item.id}`

    return allowedSlots.size > 1 ? (
        <SplitButton title="Equip" onClick={equipDefault} id={id}>
            {allowedSlots.map(slot => (
                <MenuItem key={slot} onClick={() => equipItem(item.id, slot)}>
                    {slots[slot].label}
                </MenuItem>
            )).toArray()}
        </SplitButton>
    ) : (
        <Button onClick={equipDefault}>
            Equip
        </Button>
    )
}

const EquipmentItem = ({equipped, group, item, kind, equipItem}) => {
    const equipment = item[group.prop]
    const stats = equipment.stats
    const allowedSlots = group.getSlots(equipment)

    return (
        <tr className={equipped ? 'warning' : null}>
            <td>{kind.name}</td>
            <td>{stats.melee.attack}</td>
            <td>{stats.melee.damage}</td>
            <td>{stats.ranged.attack}</td>
            <td>{stats.ranged.damage}</td>
            <td>{stats.ranged.range}</td>
            <td>{stats.defence}</td>
            <td>{stats.armor}</td>
            <td>{stats.sight.range}</td>
            <td><EquipButton item={item} allowedSlots={allowedSlots} equipItem={equipItem} /></td>
        </tr>
    )
}

const EquipmentGroup = ({equipments, group, items, kinds, equipItem}) => (
    <Panel collapsible defaultExpanded header={group.label}>
        {items.size === 0
            ? (
                <Empty />
            ) : (
                <Table condensed striped fill>
                    <thead>
                        <tr>
                            <td></td>
                            <td>Attack</td>
                            <td>Damage</td>
                            <td>Ranged attack</td>
                            <td>Ranged damage</td>
                            <td>Range</td>
                            <td>Defence</td>
                            <td>Armor</td>
                            <td>Sight</td>
                            <td></td>
                        </tr>
                    </thead>
                    <tbody>
                        {items.map(item =>
                            <EquipmentItem
                                key={item.id}
                                equipped={equipments.contains(item.id)}
                                group={group}
                                item={item}
                                kind={kinds.get(item.kind)}
                                equipItem={equipItem}
                                />
                        ).toArray()}
                    </tbody>
                </Table>
            )
        }
    </Panel>
)

const Equipments = ({equipments, inventory, kinds}) => {
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

const Inventory = ({equipments, inventory, kinds, equipItem}) =>  {
    // todo: should be at reducer but is here for performance because of bad api
    inventory = Map(inventory.map(x => [x.id, x]))
    equipments = Map(equipments.map(x => [x.key, x.value]))

    const sorter = createSorter(kinds.items)

    const renderEquipmentGroup = group => (
        <EquipmentGroup
            key={group.prop}
            equipments={equipments}
            group={group}
            items={inventory.filter(createFilter(group.prop)).sort(sorter)}
            kinds={kinds.items}
            equipItem={equipItem}
            />
    )

    return (
        <Row>
            <Col xs={4}>
                <Equipments
                    equipments={equipments}
                    inventory={inventory}
                    kinds={kinds.items}
                    />
            </Col>
            <Col xs={8}>
                {Map(groups).map(renderEquipmentGroup).toArray()}
            </Col>
        </Row>
    )
}

export default Inventory
