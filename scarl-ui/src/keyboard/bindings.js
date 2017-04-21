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
        [keycodes.ENTER, commands.PASS],
        [keycodes.NUMPAD_5, commands.PASS],
        [keycodes.L, commands.LOOK],
        [keycodes.T, commands.COMMUNICATE],
    ]).merge(directions),

    [gameModes.LOOK]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.L, commands.CANCEL_MODE],
    ]).merge(directions),
}
