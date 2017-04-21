import * as commands from '../../keyboard/commands'
import { isDirectionCommand, getDirectionLocation } from '../../keyboard/utils'
import * as gameActions from '../gameActions'

export default (command, dispatch, getState) => {
    const cursor = getState().ui.game.cursor

    if (isDirectionCommand(command)) {
        moveCursor(command, cursor, dispatch)
    } else if (command === commands.CANCEL_MODE) {
        gameActions.cancelMode()(dispatch)
    }
}

const moveCursor = (command, cursor, dispatch) => {
    const location = getDirectionLocation(command, cursor)

    gameActions.setCursorLocation(location)(dispatch)
}
