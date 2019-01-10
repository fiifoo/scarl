import * as commands from '../../keyboard/commands'
import { getTabRecipes, hasCraftingResources, tabs } from '../../game/crafting'
import { getEquipmentSet, isSetEquipmentSetCommand } from '../../keyboard/utils'
import { setCraftingRow, setCraftingTab } from '../craftingActions'
import * as gameActions from '../gameActions'
import { craftItem } from '../playerActions'

const getRecipe = getState => {
    const row = getState().ui.crafting.row

    return getRecipes(getState).get(row)
}

const getRecipes = getState => {
    const { kinds, player, recipes, ui } = getState()
    const tab = tabs.get(ui.crafting.tab)

    return getTabRecipes(player, recipes, kinds)(tab)
}

const changeTab = (dispatch, getState, left) => {
    const current = getState().ui.crafting.tab
    const next = left ? (
        current === 0 ? tabs.size - 1 : current - 1
    ) : (
        current + 1 === tabs.size ? 0 : current + 1
    )

    dispatch(setCraftingTab(next))
}

const changeRow = (dispatch, getState, up) => {
    const current = getState().ui.crafting.row
    const count = getRecipes(getState).size

    if (count === 0) {
        return
    }

    const next = up ? (
        current <= 0 ? count - 1 : current - 1
    ) : (
        current + 1 >= count ? 0 : current + 1
    )

    dispatch(setCraftingRow(next))
}

const craft = (dispatch, getState) => {
    const { player, inventory } = getState()
    const recipe = getRecipe(getState)

    if (recipe && hasCraftingResources(player, inventory)(recipe)) {
        craftItem(recipe.id)(dispatch, getState)
    }
}

export default (command, dispatch, getState) => {
    switch (command) {
        case commands.CANCEL_MODE: {
            gameActions.cancelMode()(dispatch)
            break
        }
        case commands.INVENTORY: {
            gameActions.inventory()(dispatch)
            break
        }
        case commands.PLAYER_INFO: {
            gameActions.playerInfo()(dispatch)
            break
        }
        case commands.WORLD: {
            gameActions.world()(dispatch)
            break
        }
        case commands.CRAFT: {
            craft(dispatch, getState)
            break
        }
        case commands.TAB_LEFT: {
            changeTab(dispatch, getState, true)
            break
        }
        case commands.TAB_RIGHT: {
            changeTab(dispatch, getState, false)
            break
        }
        case commands.ROW_UP: {
            changeRow(dispatch, getState, true)
            break
        }
        case commands.ROW_DOWN: {
            changeRow(dispatch, getState, false)
            break
        }
        default: {
            if (isSetEquipmentSetCommand(command)) {
                gameActions.setEquipmentSet(getEquipmentSet(command))()
            }
        }
    }
}
