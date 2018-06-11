import React from 'react'
import { Nav, NavItem } from 'react-bootstrap'
import { groups } from '../../game/equipment'
import { createItemReader, tabs } from '../../game/inventory'
import Details from './Details.jsx'
import Equipments from './Equipments.jsx'
import Equipped from './Equipped.jsx'
import Others from './Others.jsx'
import Usables from './Usables.jsx'

import './Inventory.css'

const NoItems = () => <i style={{marginLeft: 20}}>No items</i>

const Inventory = ({equipments, inventory, kinds, ui, dropItem, equipItem, unequipItem, useItem, setRow, setTab}) => {
    const tab = tabs.get(ui.tab)
    const items = createItemReader(inventory, kinds.items)(tab).toList()
    const item = items.get(ui.row)

    const list = items.isEmpty() ? <NoItems /> : (() => {
        switch (tab.key) {
            case 'Other': {
                return <Others
                    items={items}
                    kinds={kinds.items}
                    selected={ui.row}
                    dropItem={dropItem}
                    setRow={setRow} />
            }
            case 'Usable': {
                return <Usables
                    items={items}
                    kinds={kinds.items}
                    row={ui.row}
                    dropItem={dropItem}
                    useItem={useItem}
                    setRow={setRow} />
            }
            default: {
                return <Equipments
                    equipments={equipments}
                    group={groups[tab.key]}
                    items={items}
                    kinds={kinds.items}
                    row={ui.row}
                    dropItem={dropItem}
                    equipItem={equipItem}
                    unequipItem={unequipItem}
                    setRow={setRow} />
            }
        }
    })()


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
                <div className="inventory-details">
                    {item ? (
                        <Details
                            equipments={equipments}
                            inventory={inventory}
                            item={item}
                            kinds={kinds}
                            tab={tab} />
                    ) : '\u00A0'}
                </div>
                <div className="inventory-equipped">
                    <h4>Equipment</h4>
                    <Equipped
                        equipments={equipments}
                        inventory={inventory}
                        kinds={kinds.items}
                        selected={item ? item.id : null} />
                </div>
            </div>

            <div className="inventory-list">
                {list}
            </div>
        </div>
    )
}

export default Inventory
