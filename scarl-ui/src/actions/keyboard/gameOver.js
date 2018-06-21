import * as commands from '../../keyboard/commands'
import { gameOverScreen } from '../gameActions'
import { closeConnection } from '../connectionActions'

export default (command, dispatch) => {
    switch (command) {
        case commands.SHOW_GAME_OVER_SCREEN: {
            gameOverScreen()(dispatch)
            break
        }
        case commands.QUIT_GAME: {
            closeConnection()(dispatch)
            break
        }
    }
}
