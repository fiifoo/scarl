import * as commands from '../../keyboard/commands'
import * as gameActions from '../gameActions'

export default (command, dispatch, getState) => {
    switch (command) {
        case commands.CANCEL_MODE: {
            gameActions.cancelMode()(dispatch)
            break
        }
        case commands.INTERACT_NEXT: {
            gameActions.nextInteraction()(dispatch, getState)
            break
        }
        case commands.INTERACT_SELECT: {
            gameActions.selectInteraction()(dispatch, getState)
            break
        }
    }
}
