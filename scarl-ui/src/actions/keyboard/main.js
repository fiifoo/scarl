import { List } from 'immutable'
import { interactions } from '../../game/interaction'
import { getLocationCreatures, getLocationDoor, isEnemyChecker } from '../../game/utils'
import * as commands from '../../keyboard/commands'
import { getDirectionLocation, getEquipmentSet, getQuickItemSlot, isDirectionCommand, isSetEquipmentSetCommand, isUseQuickItemCommand } from '../../keyboard/utils'
import * as gameActions from '../gameActions'
import * as playerActions from '../playerActions'

export default (command, dispatch, getState) => {
    if (isDirectionCommand(command)) {
        directionAction(command, dispatch, getState)

        return
    }

    switch (command) {
        case commands.AIM: {
            gameActions.aim()(dispatch, getState)
            break
        }
        case commands.AIM_MISSILE: {
            gameActions.aimMissile()(dispatch, getState)
            break
        }
        case commands.CRAFTING: {
            gameActions.crafting()(dispatch)
            break
        }
        case commands.AUTO_MOVE: {
            gameActions.autoMove()(dispatch)
            break
        }
        case commands.COMMUNICATE: {
            gameActions.interact(interactions.Communicate)(dispatch, getState)
            break
        }
        case commands.ENTER_CONDUIT: {
            playerActions.enterConduit()(dispatch, getState)
            break
        }
        case commands.INVENTORY: {
            gameActions.inventory()(dispatch)
            break
        }
        case commands.INTERACT: {
            gameActions.interact()(dispatch, getState)
            break
        }
        case commands.KEY_BINDINGS: {
            gameActions.keyBindings()(dispatch)
            break
        }
        case commands.LOOK: {
            gameActions.look()(dispatch, getState)
            break
        }
        case commands.MENU: {
            gameActions.menu()(dispatch)
            break
        }
        case commands.MESSAGE_LOG: {
            gameActions.messageLog()(dispatch)
            break
        }
        case commands.PASS: {
            playerActions.pass()(dispatch, getState)
            break
        }
        case commands.PICK_ITEM: {
            gameActions.interact(interactions.PickItem)(dispatch, getState)
            break
        }
        case commands.PLAYER_INFO: {
            gameActions.playerInfo()(dispatch)
            break
        }
        case commands.RECYCLE_ITEM: {
            gameActions.interact(interactions.RecycleItem)(dispatch, getState)
            break
        }
        case commands.SIGNAL_MAP: {
            gameActions.signalMap()(dispatch, getState)
            break
        }
        case commands.USE: {
            gameActions.interact(List([
                interactions.HackCreature,
                interactions.HackItem,
                interactions.UseCreature,
                interactions.UseDoor,
                interactions.UseItem
            ]))(dispatch, getState)
            break
        }
        default: {
            if (isSetEquipmentSetCommand(command)) {
                gameActions.setEquipmentSet(getEquipmentSet(command))()
            } else if (isUseQuickItemCommand(command)) {
                gameActions.useQuickItem(getQuickItemSlot(command))(dispatch, getState)
            }
        }
    }
}

const directionAction = (command, dispatch, getState) => {
    const {factions, fov, player} = getState()

    const to = getDirectionLocation(command, player.creature.location)
    const creatures = getLocationCreatures(to, fov.cumulative)
    const door = getLocationDoor(to, fov.cumulative)

    if (creatures.length > 0) {
        const isEnemy = isEnemyChecker(player, factions)
        const enemies = creatures.filter(isEnemy)
        if (enemies.length > 0) {
            const enemy = enemies[0]
            playerActions.attack(enemy.id)(dispatch, getState)
        } else {
            const friend = creatures[0]
            playerActions.displace(friend.id)(dispatch, getState)
        }
    } else if (door && !door.door.open) {
        playerActions.useDoor(door.id)(dispatch, getState)
    } else {
        playerActions.move(to)(dispatch, getState)
    }
}
