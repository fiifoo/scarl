import { Map } from 'immutable'
import React from 'react'
import { Table } from 'react-bootstrap'
import { slots } from '../../game/equipment'

const Empty = () => <i>Empty</i>

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

export default Equipments
