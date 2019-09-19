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

const select = Map([
    [keycodes.NUM_1, commands.SELECT_1],
    [keycodes.NUM_2, commands.SELECT_2],
    [keycodes.NUM_3, commands.SELECT_3],
    [keycodes.NUM_4, commands.SELECT_4],
    [keycodes.NUM_5, commands.SELECT_5],
    [keycodes.NUM_6, commands.SELECT_6],
    [keycodes.NUM_7, commands.SELECT_7],
    [keycodes.NUM_8, commands.SELECT_8],
    [keycodes.NUM_9, commands.SELECT_9],
    [keycodes.NUM_0, commands.SELECT_10],
])

const setEquipmentSets = Map([
    [keycodes.Q, commands.SET_EQUIPMENT_SET_1],
    [keycodes.W, commands.SET_EQUIPMENT_SET_2],
    [keycodes.E, commands.SET_EQUIPMENT_SET_3],
])

const setQuickItems = Map([
    [keycodes.NUM_1, commands.SET_QUICK_ITEM_1],
    [keycodes.NUM_2, commands.SET_QUICK_ITEM_2],
    [keycodes.NUM_3, commands.SET_QUICK_ITEM_3],
    [keycodes.NUM_4, commands.SET_QUICK_ITEM_4],
    [keycodes.NUM_5, commands.SET_QUICK_ITEM_5],
    [keycodes.NUM_6, commands.SET_QUICK_ITEM_6],
    [keycodes.NUM_7, commands.SET_QUICK_ITEM_7],
    [keycodes.NUM_8, commands.SET_QUICK_ITEM_8],
    [keycodes.NUM_9, commands.SET_QUICK_ITEM_9],
    [keycodes.NUM_0, commands.SET_QUICK_ITEM_10],
])

const useQuickItems = Map([
    [keycodes.NUM_1, commands.USE_QUICK_ITEM_1],
    [keycodes.NUM_2, commands.USE_QUICK_ITEM_2],
    [keycodes.NUM_3, commands.USE_QUICK_ITEM_3],
    [keycodes.NUM_4, commands.USE_QUICK_ITEM_4],
    [keycodes.NUM_5, commands.USE_QUICK_ITEM_5],
    [keycodes.NUM_6, commands.USE_QUICK_ITEM_6],
    [keycodes.NUM_7, commands.USE_QUICK_ITEM_7],
    [keycodes.NUM_8, commands.USE_QUICK_ITEM_8],
    [keycodes.NUM_9, commands.USE_QUICK_ITEM_9],
    [keycodes.NUM_0, commands.USE_QUICK_ITEM_10],
])

export default {
    [gameModes.MAIN]: Map([
        [keycodes.COMMA, commands.PICK_ITEM],
        [keycodes.CONTROL, commands.INTERACT],
        [keycodes.ENTER, commands.PASS],
        [keycodes.ESC, commands.MENU],
        [keycodes.LESS, commands.ENTER_CONDUIT],
        [keycodes.LESS2, commands.ENTER_CONDUIT],
        [keycodes.LESS3, commands.ENTER_CONDUIT],
        [keycodes.NUMPAD_5, commands.PASS],
        [keycodes.PERIOD, commands.RECYCLE_ITEM],
        [keycodes.SPACEBAR, commands.AIM],
        [keycodes.A, commands.AUTO_MOVE],
        [keycodes.C, commands.CRAFTING],
        [keycodes.I, commands.INVENTORY],
        [keycodes.K, commands.KEY_BINDINGS],
        [keycodes.L, commands.LOOK],
        [keycodes.M, commands.MESSAGE_LOG],
        [keycodes.P, commands.PLAYER_INFO],
        [keycodes.S, commands.SIGNAL_MAP],
        [keycodes.T, commands.COMMUNICATE],
        [keycodes.U, commands.USE],
        [keycodes.Z, commands.AIM_MISSILE],
        [keycodes.TAB, commands.WORLD],
    ]).merge(directions).merge(setEquipmentSets).merge(useQuickItems),

    [gameModes.AIM]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.SPACEBAR, commands.SHOOT],
    ]).merge(directions),

    [gameModes.AIM_MISSILE]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.CONTROL, commands.NEXT_MISSILE],
        [keycodes.SPACEBAR, commands.SHOOT],
        [keycodes.Z, commands.SHOOT],
    ]).merge(directions),

    [gameModes.AUTO_MOVE]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.A, commands.AUTO_EXPLORE],
    ]).merge(directions),

    [gameModes.COMMUNICATE]: Map([
        [keycodes.ESC, commands.END_COMMUNICATION],
        [keycodes.ENTER, commands.SELECT_DEFAULT_OR_END_COMMUNICATION],
        [keycodes.SPACEBAR, commands.SELECT_DEFAULT_OR_END_COMMUNICATION],
    ]).merge(select),

    [gameModes.CRAFTING]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.C, commands.CANCEL_MODE],

        [keycodes.I, commands.INVENTORY],
        [keycodes.P, commands.PLAYER_INFO],
        [keycodes.TAB, commands.WORLD],

        [keycodes.ENTER, commands.CRAFT],
        [keycodes.SPACEBAR, commands.CRAFT],

        [keycodes.ARROW_LEFT, commands.TAB_LEFT],
        [keycodes.ARROW_UP, commands.ROW_UP],
        [keycodes.ARROW_RIGHT, commands.TAB_RIGHT],
        [keycodes.ARROW_DOWN, commands.ROW_DOWN],

        [keycodes.NUMPAD_2, commands.ROW_DOWN],
        [keycodes.NUMPAD_4, commands.TAB_LEFT],
        [keycodes.NUMPAD_6, commands.TAB_RIGHT],
        [keycodes.NUMPAD_8, commands.ROW_UP],
    ]).merge(setEquipmentSets),

    [gameModes.GAME_OVER]: Map([
        [keycodes.ENTER, commands.SHOW_GAME_OVER_SCREEN],
        [keycodes.ESC, commands.SHOW_GAME_OVER_SCREEN],
    ]),

    [gameModes.GAME_OVER_SCREEN]: Map([
        [keycodes.ENTER, commands.QUIT_GAME],
        [keycodes.ESC, commands.QUIT_GAME],
    ]),

    [gameModes.INTERACT]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],

        [keycodes.ARROW_UP, commands.INTERACT_PREVIOUS],
        [keycodes.NUMPAD_8, commands.INTERACT_PREVIOUS],
        [keycodes.ARROW_DOWN, commands.INTERACT_NEXT],
        [keycodes.NUMPAD_2, commands.INTERACT_NEXT],

        [keycodes.CONTROL, commands.INTERACT_SELECT],
        [keycodes.ENTER, commands.INTERACT_SELECT],
        [keycodes.SPACEBAR, commands.INTERACT_SELECT],
        [keycodes.COMMA, commands.INTERACT_SELECT],
        [keycodes.LESS, commands.INTERACT_SELECT],
        [keycodes.LESS2, commands.INTERACT_SELECT],
        [keycodes.LESS3, commands.INTERACT_SELECT],
        [keycodes.PERIOD, commands.INTERACT_SELECT],
        [keycodes.T, commands.INTERACT_SELECT],
        [keycodes.U, commands.INTERACT_SELECT],
    ]),

    [gameModes.INVENTORY]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.I, commands.CANCEL_MODE],

        [keycodes.C, commands.CRAFTING],
        [keycodes.P, commands.PLAYER_INFO],
        [keycodes.TAB, commands.WORLD],

        [keycodes.CONTROL, commands.INVENTORY_INTERACT],
        [keycodes.ENTER, commands.INVENTORY_USE],
        [keycodes.SPACEBAR, commands.INVENTORY_USE],

        [keycodes.ARROW_LEFT, commands.TAB_LEFT],
        [keycodes.ARROW_UP, commands.ROW_UP],
        [keycodes.ARROW_RIGHT, commands.TAB_RIGHT],
        [keycodes.ARROW_DOWN, commands.ROW_DOWN],

        [keycodes.NUMPAD_2, commands.ROW_DOWN],
        [keycodes.NUMPAD_4, commands.TAB_LEFT],
        [keycodes.NUMPAD_6, commands.TAB_RIGHT],
        [keycodes.NUMPAD_8, commands.ROW_UP],
    ]).merge(setEquipmentSets).merge(setQuickItems),

    [gameModes.KEY_BINDINGS]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.K, commands.CANCEL_MODE],
    ]),

    [gameModes.LOOK]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.L, commands.CANCEL_MODE],
        [keycodes.CONTROL, commands.TOGGLE_LOOK_DETAILED],
        [keycodes.ENTER, commands.TOGGLE_LOOK_DETAILED],
        [keycodes.SPACEBAR, commands.TOGGLE_LOOK_DETAILED],
    ]).merge(directions),

    [gameModes.MESSAGE_LOG]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.M, commands.CANCEL_MODE],
    ]),

    [gameModes.MENU]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
    ]),

    [gameModes.PLAYER_INFO]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.P, commands.CANCEL_MODE],

        [keycodes.C, commands.CRAFTING],
        [keycodes.I, commands.INVENTORY],
        [keycodes.TAB, commands.WORLD],
    ]).merge(setEquipmentSets),

    [gameModes.SIGNAL_MAP]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.S, commands.CANCEL_MODE],
    ]),

    [gameModes.WORLD]: Map([
        [keycodes.ESC, commands.CANCEL_MODE],
        [keycodes.TAB, commands.CANCEL_MODE],

        [keycodes.C, commands.CRAFTING],
        [keycodes.I, commands.INVENTORY],
        [keycodes.P, commands.PLAYER_INFO],
    ]),
}
