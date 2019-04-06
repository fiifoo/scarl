import { AIM_MISSILE } from '../../game/modes'
import { getMissile, getMissiles } from '../../game/utils'
import * as commands from '../../keyboard/commands'
import { isDirectionCommand, getDirectionLocation } from '../../keyboard/utils'
import * as gameActions from '../gameActions'
import * as playerActions from '../playerActions'

export default (command, dispatch, getState) => {
    const state = getState()
    const mode = state.ui.game.mode
    const reticule = state.ui.game.reticule
    const missile = mode === AIM_MISSILE && getMissile(state)

    if (isDirectionCommand(command)) {
        moveReticule(command, reticule, missile, dispatch, getState)

        return
    }

    switch (command) {
        case commands.CANCEL_MODE: {
            gameActions.cancelMode()(dispatch)
            break
        }
        case commands.NEXT_MISSILE: {
            const nextMissile = getNextMissile(getMissiles(state.player), missile)

            gameActions.setMissile(nextMissile)(dispatch)
            break
        }
        case commands.SHOOT: {
            playerActions.shoot(reticule, missile)(dispatch, getState)
            break
        }
    }
}

const getNextMissile = (missiles, missile) => {
    if (missiles.length === 0) {
        return null
    }

    const index = missiles.indexOf(missile) + 1

    if (index < missiles.length) {
        return missiles[index]
    } else {
        return missiles[0]
    }
}

const moveReticule = (command, reticule, missile, dispatch, getState) => {
    const location = getDirectionLocation(command, reticule)

    gameActions.setReticule(location, missile)(dispatch, getState)
}
