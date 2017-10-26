import { connect } from 'react-redux'
import { setInventoryRow, setInventoryTab } from '../actions/inventoryActions'
import { dropItem, equipItem, unequipItem, useInventoryItem } from '../actions/playerActions'
import GameView from '../components/GameView.jsx'
import Inventory from '../components/inventory/Inventory.jsx'
import { INVENTORY } from '../game/modes'

const InventoryContainer = connect(
    state => ({
        component: Inventory,
        visible: state.ui.game.mode === INVENTORY,

        equipments: state.equipments,
        inventory: state.inventory,
        kinds: state.kinds,
        ui: state.ui.inventory,
    }), {
        dropItem,
        equipItem,
        unequipItem,
        useItem: useInventoryItem,
        setRow: setInventoryRow,
        setTab: setInventoryTab,
    }
)(GameView)

export default InventoryContainer
