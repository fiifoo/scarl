import { List } from 'immutable'
import { interactions } from '../../game/interaction'
import { getLocationCreature, getLocationDoor, isEnemyChecker } from '../../game/utils'
import * as commands from '../../keyboard/commands'
import { isDirectionCommand, getDirectionLocation } from '../../keyboard/utils'
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
        case commands.MESSAGE_LOG: {
            gameActions.messageLog()(dispatch)
            break
        }
        case commands.PASS: {
            playerActions.pass()(dispatch)
            break
        }
        case commands.PICK_ITEM: {
            gameActions.interact(interactions.PickItem)(dispatch, getState)
            break
        }
        case commands.USE: {
            gameActions.interact(List([interactions.UseCreature, interactions.UseItem]))(dispatch, getState)
            break
        }
        case commands.USE_DOOR: {
            gameActions.interact(interactions.UseDoor)(dispatch, getState)
            break
        }
    }
}

const directionAction = (command, dispatch, getState) => {
    const {factions, fov, player} = getState()

    const to = getDirectionLocation(command, player.creature.location)
    const target = getLocationCreature(to, fov.cumulative)
    const door = getLocationDoor(to, fov.cumulative)

    if (target) {
        if (isEnemyChecker(player, factions)(target)) {
            playerActions.attack(target.id)()
        } else {
            playerActions.displace(target.id)()
        }
    } else if (door && !door.door.open) {
        playerActions.useDoor(door.id)(dispatch, getState)
    } else {
        playerActions.move(to)(dispatch)
    }
}
