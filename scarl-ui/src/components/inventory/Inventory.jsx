import React from 'react'
import { Nav, NavItem } from 'react-bootstrap'
import { getItemActionsFlat, getTabItems, tabs } from '../../game/inventory'
import Details from './Details.jsx'
import Equipped from './Equipped.jsx'
import List from './List.jsx'

import './Inventory.css'

const NoItems = () => <i style={{marginLeft: 20}}>No items</i>

const Inventory = ({equipments, inventory, kinds, ui, setAction, setRow, setTab, ...actions}) => {
    const tab = tabs.get(ui.tab)
    const items = getTabItems(inventory, kinds.items)(tab).toList()
    const item = items.get(ui.row)

    return (
        <div className="inventory">

            <Nav bsStyle="pills" activeKey={ui.tab} onSelect={setTab}>
                {tabs.map((tab, index) => (
                    <NavItem key={index} eventKey={index}>
                        {tab.label}
                    </NavItem>
                ))}
            </Nav>

            <div className="inventory-panels">
                <div className="scarl-panel">
                    {item ? (
                        <Details
                            action={ui.action}
                            actions={getItemActionsFlat(actions, equipments, tab)(item)}
                            equipments={equipments}
                            inventory={inventory}
                            item={item}
                            kinds={kinds}
                            setAction={setAction} />
                    ) : '\u00A0'}
                </div>
                <div className="scarl-panel">
                    <Equipped
                        equipments={equipments}
                        inventory={inventory}
                        kinds={kinds.items}
                        selected={item ? item.id : null} />
                </div>
            </div>

            <div className="inventory-list">
                {items.isEmpty() ? <NoItems /> : (
                    <List
                        equipments={equipments}
                        items={items}
                        kinds={kinds.items}
                        row={ui.row}
                        tab={tab}
                        setRow={setRow}
                        actions={actions} />
                )}
            </div>
        </div>
    )
}

export default Inventory
