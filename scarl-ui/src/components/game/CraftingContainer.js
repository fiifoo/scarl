import { connect } from 'react-redux'
import { setEquipmentSet } from '../../actions/gameActions'
import { cancelRecycleItem, craftItem, recycleInventoryItem } from '../../actions/playerActions'
import { setCraftingRow, setCraftingTab } from '../../actions/craftingActions'
import { CRAFTING } from '../../game/modes'
import Crafting from './Crafting.jsx'
import GameView from './GameView.jsx'

const CraftingContainer = connect(
    state => ({
        component: Crafting,
        visible: state.ui.game.mode === CRAFTING,

        equipmentSet: state.settings.equipmentSet,
        equipments: state.equipments,
        inventory: state.inventory,
        kinds: state.kinds,
        player: state.player,
        recipes: state.recipes,
        ui: state.ui.crafting,
    }), {
        cancelRecycleItem,
        craftItem,
        recycleItem: recycleInventoryItem,
        setEquipmentSet,
        setRow: setCraftingRow,
        setTab: setCraftingTab,
    }
)(GameView)

export default CraftingContainer
