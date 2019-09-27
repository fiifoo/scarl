import { getStanceSelection, isChangeStanceAllowed } from '../../game/stance'
import { getPlayerStats } from '../../game/utils'
import * as commands from '../../keyboard/commands'
import { getSelectValue, isSelectCommand } from '../../keyboard/utils'
import { cancelMode } from '../gameActions'
import { changeStance } from '../playerActions'

export default (command, dispatch, getState) => {
    switch (command) {
        case commands.CANCEL_MODE: {
            cancelMode()(dispatch)
            break
        }
        default: {
            if (isSelectCommand(command)) {
                const value = getSelectValue(command)

                select(value, dispatch, getState)
            }
        }
    }
}

const select = (value, dispatch, getState) => {
    const {player} = getState()

    if (! isChangeStanceAllowed(player.creature)) {
        return
    }

    const stances = getPlayerStats(player).stances
    const stance = getStanceSelection(stances)(value)

    if (stance !== undefined) {
        changeStance(stance)(dispatch, getState)
    }
}
