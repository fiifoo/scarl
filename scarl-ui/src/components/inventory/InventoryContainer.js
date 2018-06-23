import { connect } from 'react-redux'
import { setInventoryAction, setInventoryRow, setInventoryTab } from '../../actions/inventoryActions'
import { dropItem, equipItem, unequipItem, useInventoryItem } from '../../actions/playerActions'
import { INVENTORY } from '../../game/modes'
import GameView from '../game/GameView.jsx'
import Inventory from './Inventory.jsx'

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
        setAction: setInventoryAction,
        setRow: setInventoryRow,
        setTab: setInventoryTab,
    }
)(GameView)

export default InventoryContainer
