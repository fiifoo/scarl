import * as modes from '../../game/modes'
import bindings from '../../keyboard/bindings'
import * as types from '../actionTypes'
import aim from './aim'
import main from './main'
import look from './look'

const handlers = {
    [modes.MAIN]: main,
    [modes.AIM]: aim,
    [modes.LOOK]: look,
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
