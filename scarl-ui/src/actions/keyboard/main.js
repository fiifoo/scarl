import { getLocationCreature, isEnemyChecker } from '../../game/utils'
import * as commands from '../../keyboard/commands'
import { isDirectionCommand, getDirectionLocation } from '../../keyboard/utils'
import * as gameActions from '../gameActions'
import * as playerActions from '../playerActions'

export default (command, dispatch, getState) => {
    const {factions, fov, player} = getState()

    if (isDirectionCommand(command)) {
        directionAction(command, player, factions, fov, dispatch)

        return
    }

    switch (command) {
        case commands.AIM: {
            gameActions.aim()(dispatch, getState)
            break
        }
        case commands.COMMUNICATE: {
            playerActions.communicate()(dispatch, getState)
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
            playerActions.pickItem()(dispatch, getState)
            break
        }
    }
}

const directionAction = (command, player, factions, fov, dispatch) => {
    const to = getDirectionLocation(command, player.creature.location)
    const target = getLocationCreature(to, fov.cumulative)

    if (target && isEnemyChecker(player, factions)(target)) {
        playerActions.attack(target.id)(dispatch)
    } else {
        playerActions.move(to)(dispatch)
    }
}
