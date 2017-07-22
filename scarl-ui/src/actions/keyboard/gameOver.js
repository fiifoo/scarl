import * as commands from '../../keyboard/commands'
import * as gameActions from '../gameActions'

export default (command, dispatch) => {
    if (command === commands.SHOW_GAME_OVER_SCREEN) {
        gameActions.gameOverScreen()(dispatch)
    }
}
