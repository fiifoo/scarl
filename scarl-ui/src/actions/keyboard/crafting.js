import * as commands from '../../keyboard/commands'
import { COLUMN_RECIPES, COLUMN_RECYCLES, hasCraftingResources } from '../../game/crafting'
import { setCraftingSelection } from '../craftingActions'
import * as gameActions from '../gameActions'
import { cancelRecycleItem, craftItem } from '../playerActions'

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

const getRecycledItem = getState => {
    const row = getState().ui.crafting.row

    return getState().player.recycledItems.toArray()[row]
}

const changeRow = (dispatch, getState, up) => {
    const column = getState().ui.crafting.column
    const current = getState().ui.crafting.row
    const count = column === COLUMN_RECIPES ? getState().player.recipes.size : getState().player.recycledItems.size

    if (count === 0) {
        return
    }

    const next = up ? (
        current <= 0 ? count - 1 : current - 1
    ) : (
        current + 1 >= count ? 0 : current + 1
    )

    dispatch(setCraftingSelection(column, next))
}

const changeColumn = (dispatch, getState) => {
    const current = getState().ui.crafting.column
    const next = current === COLUMN_RECIPES ? COLUMN_RECYCLES : COLUMN_RECIPES

    dispatch(setCraftingSelection(next, 0))
}

const craft = (dispatch, getState) => {
    const { player, inventory } = getState()
    const recipe = getRecipe(getState)

    if (recipe && hasCraftingResources(player, inventory)(recipe)) {
        craftItem(recipe.id)()
    }
}

const cancelRecycle = (dispatch, getState) => {
    const { kinds, player } = getState()
    const item = getRecycledItem(getState)

    if (item) {
        const kind = kinds.items.get(item)

        if (kind.recyclable && kind.recyclable <= player.creature.resources.components) {
            cancelRecycleItem(item)()
        }
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
        case commands.CRAFTING_ACTION: {
            const column = getState().ui.crafting.column

            if (column === COLUMN_RECIPES) {
                craft(dispatch, getState)
            } else {
                cancelRecycle(dispatch, getState)
            }
            break
        }
        case commands.CRAFTING_SELECTION_UP: {
            changeRow(dispatch, getState, true)
            break
        }
        case commands.CRAFTING_SELECTION_DOWN: {
            changeRow(dispatch, getState, false)
            break
        }
        case commands.CRAFTING_SELECTION_LEFT:
        case commands.CRAFTING_SELECTION_RIGHT: {
            changeColumn(dispatch, getState)
            break
        }
    }
}
