import { List } from 'immutable'
import { compose } from 'redux'
import * as commands from '../../keyboard/commands'
import { getEquipmentSet, getQuickItemSlot, isSetEquipmentSetCommand, isSetQuickItemCommand } from '../../keyboard/utils'

import * as gameActions from '../gameActions'
import * as inventoryActions from '../inventoryActions'
import * as playerActions from '../playerActions'
import { getItemActionsFlat, getTabItems, tabs } from '../../game/inventory'

const getItem = getState => {
    const row = getState().ui.inventory.row

    return getItems(getState).toArray()[row]
}

const getItems = getState => {
    const {inventory, kinds, ui} = getState()
    const tab = tabs.get(ui.inventory.tab)

    return getTabItems(inventory, kinds.items)(tab)
}

const getActions = (dispatch, getState) => {
    const item = getItem(getState)

    if (! item) {
        return List()
    }

    const {equipments, ui} = getState()
    const tab = tabs.get(ui.inventory.tab)

    return getItemActionsFlat(composeActions(dispatch), equipments, tab)(item)
}

const composeActions = dispatch => ({
    dropItem: compose(dispatch, playerActions.dropItem),
    equipItem: compose(dispatch, playerActions.equipItem),
    unequipItem: compose(dispatch, playerActions.unequipItem),
    useItem: compose(dispatch, playerActions.useInventoryItem),
})

const changeTab = (dispatch, getState, left) => {
    const current = getState().ui.inventory.tab
    const next = left ? (
        current === 0 ? tabs.size - 1 : current - 1
    ) : (
        current + 1 === tabs.size ? 0 : current + 1
    )

    dispatch(inventoryActions.setInventoryTab(next))
}

const changeRow = (dispatch, getState, up) => {
    const current = getState().ui.inventory.row
    const count = getItems(getState).size

    if (count === 0) {
        return
    }

    const next = up ? (
        current <= 0 ? count - 1 : current - 1
    ) : (
        current + 1 >= count ? 0 : current + 1
    )

    dispatch(inventoryActions.setInventoryRow(next))
}

const changeAction = (dispatch, getState, up) => {
    const current = getState().ui.inventory.action
    const count = getActions(dispatch, getState).size

    if (count === 0) {
        return
    }

    const next = up ? (
        current <= 0 ? count - 1 : current - 1
    ) : (
        current + 1 >= count ? 0 : current + 1
    )

    setAction(dispatch, next)
}

const setAction = (dispatch, action) => {
    dispatch(inventoryActions.setInventoryAction(action))
}

const setQuickItem = (slot, item) => {
    if (item && item.usable) {
        gameActions.setQuickItem(slot, item.kind)()
    }
}

const use = (dispatch, getState) => {
    const action = getState().ui.inventory.action || 0
    const actions = getActions(dispatch, getState)

    if (actions.isEmpty()) {
        return
    }

    actions.get(action).execute()
}

const handleCommon = (command, dispatch, getState) => {
    switch (command) {
        case commands.PLAYER_INFO: {
            gameActions.playerInfo()(dispatch)
            break
        }
        case commands.INVENTORY_TAB_LEFT: {
            changeTab(dispatch, getState, true)
            break
        }
        case commands.INVENTORY_TAB_RIGHT: {
            changeTab(dispatch, getState, false)
            break
        }
        case commands.INVENTORY_USE: {
            use(dispatch, getState)
            break
        }
    }
}

const handleNormal = (command, dispatch, getState) => {
    switch (command) {
        case commands.CANCEL_MODE: {
            gameActions.cancelMode()(dispatch)
            break
        }
        case commands.INVENTORY_ROW_UP: {
            changeRow(dispatch, getState, true)
            break
        }
        case commands.INVENTORY_ROW_DOWN: {
            changeRow(dispatch, getState, false)
            break
        }
        case commands.INVENTORY_INTERACT: {
            if (! getActions(dispatch, getState).isEmpty()) {
                setAction(dispatch, 0)
            }
            break
        }
        default: {
            if (isSetEquipmentSetCommand(command)) {
                gameActions.setEquipmentSet(getEquipmentSet(command))()
            } else if (isSetQuickItemCommand(command)) {
                setQuickItem(getQuickItemSlot(command), getItem(getState))
            }
        }
    }
}

const handleInteract = (command, dispatch, getState) => {
    switch (command) {
        case commands.CANCEL_MODE: {
            setAction(dispatch, null)
            break
        }
        case commands.INVENTORY_ROW_UP: {
            changeAction(dispatch, getState, true)
            break
        }
        case commands.INVENTORY_ROW_DOWN: {
            changeAction(dispatch, getState, false)
            break
        }
        case commands.INVENTORY_INTERACT: {
            use(dispatch, getState)
            break
        }
    }
}

export default (command, dispatch, getState) => {
    handleCommon(command, dispatch, getState)

    if (getState().ui.inventory.action !== null) {
        handleInteract(command, dispatch, getState)
    } else {
        handleNormal(command, dispatch, getState)
    }
}
