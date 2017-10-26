import * as commands from '../../keyboard/commands'
import * as gameActions from '../gameActions'
import * as inventoryActions from '../inventoryActions'
import * as playerActions from '../playerActions'
import { groups } from '../../game/equipment'
import { createItemReader, tabs } from '../../game/inventory'

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
    const {inventory, kinds, ui} = getState()
    const tab = tabs.get(ui.inventory.tab)
    const count = createItemReader(inventory, kinds.items)(tab).size

    if (count === 0) {
        return
    }

    const current = ui.inventory.row

    const next = up ? (
        current <= 0 ? count - 1 : current - 1
    ) : (
        current + 1 >= count ? 0 : current + 1
    )

    dispatch(inventoryActions.setInventoryRow(next))
}

const use = (dispatch, getState) => {
    const {equipments, inventory, kinds, ui} = getState()
    const tab = tabs.get(ui.inventory.tab)
    const item = createItemReader(inventory, kinds.items)(tab).toArray()[ui.inventory.row]

    if (! item) {
        return
    }

    switch (tab.key) {
        case 'Usable': {
            playerActions.useInventoryItem(item.id)()
            break
        }
        default: {
            const equipped = equipments.contains(item.id)
            if (equipped) {
                playerActions.unequipItem(item.id)()
            } else {
                const group = groups[tab.key]
                const slot = group.getSlots(item).first()

                playerActions.equipItem(item.id, slot)()
            }
            break
        }
    }
}

export default (command, dispatch, getState) => {
    switch (command) {
        case commands.CANCEL_MODE: {
            gameActions.cancelMode()(dispatch)
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
        case commands.INVENTORY_ROW_UP: {
            changeRow(dispatch, getState, true)
            break
        }
        case commands.INVENTORY_ROW_DOWN: {
            changeRow(dispatch, getState, false)
            break
        }
        case commands.INVENTORY_USE: {
            use(dispatch, getState)
            break
        }
    }
}
