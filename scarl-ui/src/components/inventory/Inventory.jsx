import React from 'react'
import { Tab, Tabs } from 'react-bootstrap'
import { groups } from '../../game/equipment'
import { createItemReader, tabs } from '../../game/inventory'
import Equipments from './Equipments.jsx'
import Equipped from './Equipped.jsx'
import Usables from './Usables.jsx'

import './Inventory.css'

const Inventory = ({equipments, inventory, kinds, ui, dropItem, equipItem, unequipItem, useItem, setRow, setTab}) => {
    const getItems = createItemReader(inventory, kinds.items)

    const renderEquipments = (group, items) => (
        <Equipments
            equipments={equipments}
            group={group}
            items={items}
            kinds={kinds.items}
            row={ui.row}
            dropItem={dropItem}
            equipItem={equipItem}
            unequipItem={unequipItem}
            setRow={setRow} />
    )

    const renderUsables = items => (
        <Usables
            items={items}
            kinds={kinds.items}
            row={ui.row}
            dropItem={dropItem}
            useItem={useItem}
            setRow={setRow} />
    )

    const renderTab = (tab, index) => {
        const items = getItems(tab)

        return (
            <Tab key={index} eventKey={index} title={tab.label}>
                {tab.key === 'Usable' ? (
                    renderUsables(items)
                ) : (
                    renderEquipments(groups[tab.key], items)
                )}
            </Tab>
        )
    }

    return (
        <div>
            <div className="inventory-equipped">
                <h4 className="text-center">Equipments</h4>
                <Equipped
                    equipments={equipments}
                    inventory={inventory}
                    kinds={kinds.items} />
            </div>
            <Tabs
                className="inventory-tabs"
                id="inventory-tabs"
                bsStyle="pills"
                activeKey={ui.tab}
                onSelect={setTab}>
                {tabs.map(renderTab)}
            </Tabs>
        </div>
    )
}

export default Inventory
