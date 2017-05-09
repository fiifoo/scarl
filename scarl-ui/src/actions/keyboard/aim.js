import * as commands from '../../keyboard/commands'
import { isDirectionCommand, getDirectionLocation } from '../../keyboard/utils'
import * as gameActions from '../gameActions'
import * as playerActions from '../playerActions'

export default (command, dispatch, getState) => {
    const reticule = getState().ui.game.reticule

    if (isDirectionCommand(command)) {
        moveReticule(command, reticule, dispatch, getState)

        return
    }

    switch (command) {
        case commands.CANCEL_MODE: {
            gameActions.cancelMode()(dispatch)
            break
        }
        case commands.SHOOT: {
            playerActions.shoot(reticule)(dispatch, getState)
            break
        }
    }
}

const moveReticule = (command, reticule, dispatch, getState) => {
    const location = getDirectionLocation(command, reticule)

    gameActions.setReticule(location)(dispatch, getState)
}
