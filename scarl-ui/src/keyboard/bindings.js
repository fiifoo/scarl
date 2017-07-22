import { Map } from 'immutable'
import * as commands from './commands'
import * as keycodes from './keycodes'
import * as gameModes from '../game/modes'

const directions = Map([
    [keycodes.ARROW_LEFT, commands.DIRECTION_WEST],
    [keycodes.ARROW_UP, commands.DIRECTION_NORTH],
    [keycodes.ARROW_RIGHT, commands.DIRECTION_EAST],
    [keycodes.ARROW_DOWN, commands.DIRECTION_SOUTH],

    [keycodes.NUMPAD_1, commands.DIRECTION_SOUTH_WEST],
    [keycodes.NUMPAD_2, commands.DIRECTION_SOUTH],
    [keycodes.NUMPAD_3, commands.DIRECTION_SOUTH_EAST],
    [keycodes.NUMPAD_4, commands.DIRECTION_WEST],
    [keycodes.NUMPAD_6, commands.DIRECTION_EAST],
    [keycodes.NUMPAD_7, commands.DIRECTION_NORTH_WEST],
    [keycodes.NUMPAD_8, commands.DIRECTION_NORTH],
    [keycodes.NUMPAD_9, commands.DIRECTION_NORTH_EAST],
])

export default {
    [gameModes.MAIN]: Map([
        [keycodes.COMMA, commands.PICK_ITEM],
        [keycodes.ENTER, commands.PASS],
        [keycodes.LESS, commands.ENTER_CONDUIT],
        [keycodes.LESS2, commands.ENTER_CONDUIT],
        [keycodes.NUMPAD_5, commands.PASS],
        [keycodes.SPACEBAR, commands.AIM],
        [keycodes.D, commands.USE_DOOR],
        [keycodes.I, commands.INVENTORY],
        [keycodes.K, commands.KEY_BINDINGS],
        [keycodes.L, commands.LOOK],
        [keycodes.M, commands.MESSAGE_LOG],
        [keycodes.P, commands.PICK_ITEM],
        [keycodes.T, commands.COMMUNICATE],
        [keycodes.U, commands.USE],
    ]).merge(directions),

    [gameModes.AIM]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.SPACEBAR, commands.SHOOT],
    ]).merge(directions),

    [gameModes.GAME_OVER]: Map([
        [keycodes.ENTER, commands.SHOW_GAME_OVER_SCREEN],
    ]),

    [gameModes.GAME_OVER_SCREEN]: Map(),

    [gameModes.INVENTORY]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.I, commands.CANCEL_MODE],
    ]),

    [gameModes.KEY_BINDINGS]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.K, commands.CANCEL_MODE],
    ]),

    [gameModes.LOOK]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.L, commands.CANCEL_MODE],
    ]).merge(directions),

    [gameModes.MESSAGE_LOG]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.M, commands.CANCEL_MODE],
    ]),
}
