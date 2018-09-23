import * as commands from '../../keyboard/commands'
import { hasCraftingResources } from '../../game/crafting'
import { setCraftingRow } from '../craftingActions'
import * as gameActions from '../gameActions'
import { craftItem } from '../playerActions'

const getRecipe = getState => {
    const { recipes, ui } = getState()
    const row = ui.crafting.row

    const id = getState().player.recipes.toArray()[row]

    if (id) {
        return recipes.get(id)
    } else {
        return undefined
    }
}

const changeRow = (dispatch, getState, up) => {
    const current = getState().ui.crafting.row
    const count = getState().player.recipes.size

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
        craftItem(recipe.id)()
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
        case commands.CRAFTING_CRAFT: {
            craft(dispatch, getState)
            break
        }
        case commands.CRAFTING_ROW_UP: {
            changeRow(dispatch, getState, true)
            break
        }
        case commands.CRAFTING_ROW_DOWN: {
            changeRow(dispatch, getState, false)
            break
        }
    }
}
