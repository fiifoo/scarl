import { connect } from 'react-redux'
import { setEquipmentSet, setQuickItem } from '../../actions/gameActions'
import { setInventoryAction, setInventoryRow, setInventoryTab } from '../../actions/inventoryActions'
import { dropItem, equipItem, recycleInventoryItem, unequipItem, useInventoryItem } from '../../actions/playerActions'
import { INVENTORY } from '../../game/modes'
import GameView from '../game/GameView.jsx'
import Inventory from './Inventory.jsx'

const InventoryContainer = connect(
    state => ({
        component: Inventory,
        visible: state.ui.game.mode === INVENTORY,

        equipmentSet: state.settings.equipmentSet,
        equipments: state.player.equipments,
        inventory: state.player.inventory,
        kinds: state.kinds,
        quickItems: state.settings.quickItems,
        ui: state.ui.inventory,
    }), {
        dropItem,
        equipItem,
        unequipItem,
        recycleItem: recycleInventoryItem,
        useItem: useInventoryItem,
        setAction: setInventoryAction,
        setEquipmentSet,
        setQuickItem,
        setRow: setInventoryRow,
        setTab: setInventoryTab,
    }
)(GameView)

export default InventoryContainer
