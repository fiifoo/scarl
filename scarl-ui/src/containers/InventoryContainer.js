import { connect } from 'react-redux'
import { equipItem } from '../actions/playerActions'
import Inventory from '../components/Inventory.jsx'
import LazyToggleable from '../components/LazyToggleable.jsx'
import { INVENTORY } from '../game/modes'

const InventoryContainer = connect(
    state => ({
        component: Inventory,
        visible: state.ui.game.mode === INVENTORY,

        equipments: state.equipments,
        inventory: state.inventory,
        kinds: state.kinds,
    }), {
        equipItem,
    }
)(LazyToggleable)

export default InventoryContainer
