import * as modes from '../../game/modes'
import bindings from '../../keyboard/bindings'
import * as commands from '../../keyboard/commands'
import * as types from '../actionTypes'
import * as gameActions from '../gameActions'
import aim from './aim'
import autoMove from './autoMove'
import crafting from './crafting'
import gameOver from './gameOver'
import interact from './interact'
import inventory from './inventory'
import main from './main'
import look from './look'
import playerInfo from './playerInfo'

const onlyCancelMode = (command, dispatch) => {
    if (command === commands.CANCEL_MODE) {
        gameActions.cancelMode()(dispatch)
    }
}

const handlers = {
    [modes.MAIN]: main,
    [modes.AIM]: aim(),
    [modes.AIM_MISSILE]: aim(true),
    [modes.AUTO_MOVE]: autoMove,
    [modes.CRAFTING]: crafting,
    [modes.GAME_OVER]: gameOver,
    [modes.GAME_OVER_SCREEN]: gameOver,
    [modes.INTERACT]: interact,
    [modes.INVENTORY]: inventory,
    [modes.KEY_BINDINGS]: onlyCancelMode,
    [modes.LOOK]: look,
    [modes.MESSAGE_LOG]: onlyCancelMode,
    [modes.MENU]: onlyCancelMode,
    [modes.PLAYER_INFO]: playerInfo,
}

export const focusKeyboard = () => ({
    type: types.FOCUS_KEYBOARD,
})

export const blurKeyboard = () => ({
    type: types.BLUR_KEYBOARD,
})

export const keypress = event => (dispatch, getState) => {
    const mode = getState().ui.game.mode
    const code = event.which
    const command = bindings[mode].get(code)
    const handler = handlers[mode]

    if (command !== undefined) {
        handler(command, dispatch, getState)
    }
}
