import { connect } from 'react-redux'
import { dropItem, equipItem, unequipItem, useInventoryItem } from '../actions/playerActions'
import GameView from '../components/GameView.jsx'
import Inventory from '../components/Inventory.jsx'
import { INVENTORY } from '../game/modes'

const InventoryContainer = connect(
    state => ({
        component: Inventory,
        visible: state.ui.game.mode === INVENTORY,

        equipments: state.equipments,
        inventory: state.inventory,
        kinds: state.kinds,
    }), {
        dropItem,
        equipItem,
        unequipItem,
        useItem: useInventoryItem,
    }
)(GameView)

export default InventoryContainer
