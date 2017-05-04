import { slots } from '../game/equipment'
import { seekTargets } from '../game/utils'
import * as modes from '../game/modes'
import * as types from './actionTypes'
import { addMessage } from './infoActions'

export const aim = () => (dispatch, getState) => {
    const {player} = getState()

    // todo: just check from Map once api is sensible
    const hasWeapon = player.equipments.find(x => x.key === slots.RangedSlot.key) !== undefined

    if (! hasWeapon) {
        addMessage('Nothing to aim with.')(dispatch)

        return
    }

    seekTarget()(dispatch, getState)
    changeMode(modes.AIM)(dispatch)
}

export const look = () => (dispatch, getState) => {
    const {player} = getState()

    setCursorLocation(player.creature.location)(dispatch)
    changeMode(modes.LOOK)(dispatch)
}

export const cancelMode = () => dispatch => {
    setCursorLocation(null)(dispatch)
    changeMode(modes.MAIN)(dispatch)
}

export const setCursorLocation = location => dispatch => dispatch({
    type: types.SET_CURSOR_LOCATION,
    location,
})

export const setTarget = target => dispatch => dispatch({
    type: types.SET_TARGET,
    target,
})

export const toggleInventory = () => dispatch => dispatch({
    type: types.TOGGLE_INVENTORY,
})

const seekTarget = () => (dispatch, getState) => {
    const {player, fov, ui} = getState()
    const prev = ui.game.target

    const targets = seekTargets(player, fov.cumulative)

    if (targets.length > 0) {
        const prevTarget = prev ? targets.find(target => target.id === prev) : undefined

        if (prevTarget) {
            setCursorLocation(prevTarget.location)(dispatch)
        } else {
            setCursorLocation(targets[0].location)(dispatch)
        }
    } else {
        setCursorLocation(player.creature.location)(dispatch)
    }
}

const changeMode = mode => dispatch => dispatch({
    type: types.CHANGE_GAME_MODE,
    mode,
})
