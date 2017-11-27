import React from 'react'
import { Button, Table } from 'react-bootstrap'
import { DropButton } from './buttons.jsx'

const UseButton = ({item, useItem}) => (
    <Button
        bsSize="xsmall"
        onClick={() => useItem(item.id)}>
        Use
    </Button>
)

const UsableItem = ({item, kind, selected, dropItem, useItem, select}) => (
    <tr className={selected ? 'active' : null} onClick={select}>
        <td><UseButton item={item} useItem={useItem} /></td>
        <td><DropButton item={item} dropItem={dropItem} /></td>
        <td className="full-width">{kind.name}</td>
    </tr>
)

const Usables = ({items, kinds, row, dropItem, useItem, setRow}) => (
    <Table condensed className="inventory-group">
        <tbody>
            <tr>
                <td colSpan="3"><b>Usables</b></td>
            </tr>
            {items.toArray().map((item, index) =>
                <UsableItem
                    key={item.id}
                    item={item}
                    kind={kinds.get(item.kind)}
                    selected={index === row}
                    dropItem={dropItem}
                    useItem={useItem}
                    select={() => setRow(index)} />
            )}
        </tbody>
    </Table>
)

export default Usables