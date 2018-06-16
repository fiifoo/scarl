import { Map } from 'immutable'
import React from 'react'
import { slots } from '../../game/equipment'

const Empty = () => <i>Empty</i>

const Equipped = ({equipments, inventory, kinds, selected}) => {
    const renderSlot = slot =>  {
        const item = equipments.get(slot.key)

        return (
            <tr key={slot.key} className={item && item === selected ? 'active' : null}>
                <th className="text-right">{slot.label}</th>
                <td>
                    {item
                        ? kinds.get(inventory.get(item).kind).name
                        : (<Empty />)
                    }
                </td>
            </tr>
        )
    }

    return (
        <div>
            <h4>Equipment</h4>
            <table className="scarl-table">
                <tbody>
                    {Map(slots).map(renderSlot).toArray()}
                </tbody>
            </table>
        </div>
    )
}

export default Equipped
