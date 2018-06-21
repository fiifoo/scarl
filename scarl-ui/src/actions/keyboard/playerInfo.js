import * as commands from '../../keyboard/commands'
import * as gameActions from '../gameActions'

export default (command, dispatch) => {
    switch (command) {
        case commands.CANCEL_MODE: {
            gameActions.cancelMode()(dispatch)
            break
        }
        case commands.INVENTORY: {
            gameActions.inventory()(dispatch)
            break
        }
    }
}
