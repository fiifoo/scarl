import { Range } from 'immutable'
import React from 'react'

const slots = Range(1, 11)

const Empty = () => <i>Empty</i>

const QuickItems = ({item, kinds, quickItems, setQuickItem}) => {
    const renderSlot = slot =>  {
        const kind = quickItems.get(slot)

        return (
            <tr
                key={slot}
                className={item && kind && item.kind === kind ? 'active' : null}
                onClick={() => item && setQuickItem(slot, item.kind)}>
                <th className="text-right">{slot}</th>
                <td>
                    {kind
                        ? kinds.get(kind).name
                        : (<Empty />)
                    }
                </td>
            </tr>
        )
    }

    return (
        <div>
            <h4>Quick slots</h4>
            <table className="scarl-table">
                <tbody>
                    {slots.map(renderSlot).toArray()}
                </tbody>
            </table>
        </div>
    )
}

export default QuickItems
