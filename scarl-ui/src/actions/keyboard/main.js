import { getAdjacentLocations, getLocationCreature } from '../../game/utils'
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
            communicateAction(player, fov, dispatch)
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

const communicateAction = (player, fov, dispatch) => {
    let target = undefined
    getAdjacentLocations(player.location).forEach(location => {
        if (target === undefined) {
            target = getLocationCreature(location, fov.cumulative)
        }
    })

    if (target) {
        playerActions.communicate(target.id)(dispatch)
    }
}

const directionAction = (command, player, fov, dispatch) => {
    const to = getDirectionLocation(command, player.location)
    const target = getLocationCreature(to, fov.cumulative)

    if (target) {
        playerActions.attack(target.id)(dispatch)
    } else {
        playerActions.move(to)(dispatch)
    }
}
