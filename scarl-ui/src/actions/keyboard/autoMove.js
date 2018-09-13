import * as commands from '../../keyboard/commands'
import { isDirectionCommand, getDirectionValue } from '../../keyboard/utils'
import { sendAutoMove } from '../connectionActions'
import { cancelMode } from '../gameActions'

export default (command, dispatch) => {
    if (isDirectionCommand(command)) {
        move(getDirectionValue(command))(dispatch)

        return
    }

    switch (command) {
        case commands.CANCEL_MODE: {
            cancelMode()(dispatch)
            break
        }
        case commands.AUTO_EXPLORE: {
            move()(dispatch)
            break
        }
    }
}

const move = direction => dispatch => {
    sendAutoMove(direction)
    cancelMode()(dispatch)
}
