import React from 'react'
import { Button, MenuItem, SplitButton } from 'react-bootstrap'
import { getItemActions } from '../../game/inventory'

const ActionButton = ({action, id}) => (
    action.subs.isEmpty() ? (
        <Button
            bsSize="xsmall"
            bsStyle={action.active ? 'primary' : undefined}
            onClick={action.execute}>
            {action.label}
        </Button>
    ) : (
        <SplitButton
            id={id}
            bsSize="xsmall"
            bsStyle={action.active ? 'primary' : undefined}
            dropup
            title={action.label}
            onClick={action.execute}>
            {action.subs.map((sub, index) => (
                <MenuItem key={index} onClick={sub.execute}>
                    {sub.label}
                </MenuItem>
            ))}
        </SplitButton>
    )
)

const Item = ({equipments, item, kind, selected, tab, select, actions}) => (
    <tr
        key={item.id}
        className={selected ? 'active' : null}
        onClick={select}>
        {getItemActions(actions, equipments, tab)(item).map((action, index) => (
            <td key={index}>
                <ActionButton action={action} id={`action-${item.id}-${index}`} />
            </td>
        ))}
        <td className="full-width">{kind.name}</td>
    </tr>
)

const List = ({equipments, items, kinds, row, tab, setRow, actions}) => (
    <table className="scarl-table">
        <tbody>
            {items.map((item, index) =>
                <Item
                    key={item.id}
                    equipments={equipments}
                    item={item}
                    kind={kinds.get(item.kind)}
                    selected={index === row}
                    tab={tab}
                    select={() => setRow(index)}
                    actions={actions} />
            )}
        </tbody>
    </table>
)

export default List
