import { getLocationCreature, getLocationPickableItems } from '../../game/utils'
import * as commands from '../../keyboard/commands'
import { isDirectionCommand, getDirectionLocation } from '../../keyboard/utils'
import * as gameActions from '../gameActions'
import * as playerActions from '../playerActions'

export default (command, dispatch, getState) => {
    const {player, fov} = getState()

    if (isDirectionCommand(command)) {
        directionAction(command, player, fov, dispatch)

        return
    }

    switch (command) {
        case commands.COMMUNICATE: {
            playerActions.communicate()(dispatch, getState)
            break
        }
        case commands.LOOK: {
            gameActions.look()(dispatch, getState)
            break
        }
        case commands.PASS: {
            playerActions.pass()(dispatch)
            break
        }
    }
}

const directionAction = (command, player, fov, dispatch) => {
    const to = getDirectionLocation(command, player.creature.location)
    const target = getLocationCreature(to, fov.cumulative)

    if (target) {
        playerActions.attack(target.id)(dispatch)
    } else {
        // testing
        const items = getLocationPickableItems(to, fov.cumulative)
        if (items.length > 0) {
            const item = items[0]
            playerActions.move(to)(dispatch)
            playerActions.pickItem(item.id)(dispatch)
            playerActions.equipItem(item.id, 'MainHand')(dispatch)
        } else {
            playerActions.move(to)(dispatch)
        }
    }
}
