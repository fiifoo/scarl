import { connect } from 'react-redux'
import { cancelRecycleItem, craftItem } from '../../actions/playerActions'
import { setCraftingSelection } from '../../actions/craftingActions'
import { CRAFTING } from '../../game/modes'
import Crafting from './Crafting.jsx'
import GameView from './GameView.jsx'

const CraftingContainer = connect(
    state => ({
        component: Crafting,
        visible: state.ui.game.mode === CRAFTING,

        equipments: state.equipments,
        inventory: state.inventory,
        kinds: state.kinds,
        player: state.player,
        recipes: state.recipes,
        ui: state.ui.crafting,
    }), {
        cancelRecycleItem,
        craftItem,
        setSelection: setCraftingSelection,
    }
)(GameView)

export default CraftingContainer
