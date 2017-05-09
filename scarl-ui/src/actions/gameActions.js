import { slots } from '../game/equipment'
import * as modes from '../game/modes'
import { seekTargets, calculateTrajectory } from '../game/utils'
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

    setCursor(player.creature.location)(dispatch)
    changeMode(modes.LOOK)(dispatch)
}

export const cancelMode = () => dispatch => {
    changeMode(modes.MAIN)(dispatch)
}

export const setCursor = cursor => dispatch => dispatch({
    type: types.SET_CURSOR,
    cursor,
})

export const setReticule = reticule => (dispatch, getState) => {
    const {player, fov} = getState()
    const trajectory = calculateTrajectory(player, reticule, fov.cumulative)

    dispatch({
        type: types.SET_RETICULE,
        reticule,
        trajectory,
    })
}

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
            setReticule(prevTarget.location)(dispatch, getState)
        } else {
            setReticule(targets[0].location)(dispatch, getState)
        }
    } else {
        setReticule(player.creature.location)(dispatch, getState)
    }
}

const changeMode = mode => dispatch => dispatch({
    type: types.CHANGE_GAME_MODE,
    mode,
})
