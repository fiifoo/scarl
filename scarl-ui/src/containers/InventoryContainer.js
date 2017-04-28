import { connect } from 'react-redux'
import { toggleInventory } from '../actions/gameActions'
import { equipItem } from '../actions/playerActions'
import { focusKeyboard } from '../actions/keyboard'
import Inventory from '../components/Inventory.jsx'
import LazyModal from '../components/LazyModal.jsx'

const InventoryContainer = connect(
    state => ({
        component: Inventory,
        title: 'Inventory',
        visible: state.ui.game.inventoryVisible,
        modalProps: {bsSize: 'large'},

        equipments: state.player.equipments,
        inventory: state.player.inventory,
        kinds: state.kinds,
    }), {
        focusKeyboard,
        toggle: toggleInventory,

        equipItem,
    }
)(LazyModal)

export default InventoryContainer
