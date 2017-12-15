import React from 'react'
import { Table } from 'react-bootstrap'
import { DropButton } from './buttons.jsx'

const OtherItem = ({item, kind, selected, dropItem, select}) => (
    <tr className={selected ? 'active' : null} onClick={select}>
        <td><DropButton item={item} dropItem={dropItem} /></td>
        <td className="full-width">{kind.name}</td>
    </tr>
)

const Others = ({items, kinds, row, dropItem, setRow}) => (
    <Table condensed className="inventory-group">
        <tbody>
            <tr>
                <td colSpan="2"><b>Other</b></td>
            </tr>
            {items.toArray().map((item, index) =>
                <OtherItem
                    key={item.id}
                    item={item}
                    kind={kinds.get(item.kind)}
                    selected={index === row}
                    dropItem={dropItem}
                    select={() => setRow(index)} />
            )}
        </tbody>
    </Table>
)

export default Others
