import * as commands from '../../keyboard/commands'
import { isDirectionCommand, getDirectionLocation } from '../../keyboard/utils'
import * as gameActions from '../gameActions'
import * as playerActions from '../playerActions'

export default (command, dispatch, getState) => {
    const cursor = getState().ui.game.cursor

    if (isDirectionCommand(command)) {
        moveCursor(command, cursor, dispatch)

        return
    }

    switch (command) {
        case commands.CANCEL_MODE: {
            gameActions.cancelMode()(dispatch)
            break
        }
        case commands.SHOOT: {
            playerActions.shoot(cursor)(dispatch, getState)
            break
        }
    }
}

const moveCursor = (command, cursor, dispatch) => {
    const location = getDirectionLocation(command, cursor)

    gameActions.setCursorLocation(location)(dispatch)
}
