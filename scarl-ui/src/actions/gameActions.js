import * as modes from '../game/modes'
import { seekInteractions } from '../game/interaction'
import { calculateTrajectory, getMissileLauncherRange, getRangedAttackRange, seekTargets } from '../game/utils'
import * as types from './actionTypes'
import { sendAction, sendInventoryQuery } from './connectionActions'

export const addMessage = message => dispatch => dispatch({
    type: types.ADD_MESSAGE,
    message,
})

export const aim = () => (dispatch, getState) => {
    const hasWeapon = getRangedAttackRange(getState().player) > 0

    if (! hasWeapon) {
        addMessage('No ranged weapon equipped.')(dispatch)

        return
    }

    changeMode(modes.AIM)(dispatch)
    seekTarget()(dispatch, getState)
}

export const aimMissile = () => (dispatch, getState) => {
    const hasWeapon = getMissileLauncherRange(getState().player) > 0

    if (! hasWeapon) {
        addMessage('No launcher equipped.')(dispatch)

        return
    }

    changeMode(modes.AIM_MISSILE)(dispatch)
    seekTarget(true)(dispatch, getState)
}

export const cancelMode = () => dispatch => changeMode(modes.MAIN)(dispatch)

export const gameOverScreen = () => dispatch => {
    changeMode(modes.GAME_OVER_SCREEN)(dispatch)
}

export const inventory = () => dispatch => {
    sendInventoryQuery()
    changeMode(modes.INVENTORY)(dispatch)
}

export const interact = (actions = undefined) => (dispatch, getState) => {
    const {player, fov} = getState()

    const interactions = seekInteractions(player, fov.cumulative, actions)

    if (interactions.isEmpty()) {
        addMessage('Nothing to interact with.')(dispatch)
    } else if (actions && interactions.size === 1) {
        const interaction = interactions.first()

        sendAction(interaction.action, interaction.data)
    } else {
        dispatch({
            type: types.SET_INTERACTIONS,
            interactions,
        })

        const location = interactions.first().location
        changeMode(modes.INTERACT)(dispatch)
        setCursor(location)(dispatch)
    }
}

export const keyBindings = () => dispatch => changeMode(modes.KEY_BINDINGS)(dispatch)

export const look = () => (dispatch, getState) => {
    const {player} = getState()

    changeMode(modes.LOOK)(dispatch)
    setCursor(player.creature.location)(dispatch)
}

export const messageLog = () => dispatch => changeMode(modes.MESSAGE_LOG)(dispatch)

export const menu = () => dispatch => changeMode(modes.MENU)(dispatch)

export const nextInteraction = () => (dispatch, getState) => {
    const {interaction, interactions} = getState().ui.game

    const next = interaction + 1 >= interactions.size ? 0 : interaction + 1
    const location = interactions.get(next).location

    setCursor(location)(dispatch)
    dispatch({
        type: types.SET_INTERACTION,
        interaction: next,
    })
}

export const selectInteraction = () => (dispatch, getState) => {
    const {interaction, interactions} = getState().ui.game
    const values = interactions.get(interaction)

    cancelMode()(dispatch)
    sendAction(values.action, values.data)
}

export const setCursor = cursor => dispatch => dispatch({
    type: types.SET_CURSOR,
    cursor,
})

export const setReticule = (reticule, missile = false) => (dispatch, getState) => {
    const {player, fov} = getState()
    const trajectory = calculateTrajectory(player, reticule, fov.cumulative, missile)

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

export const storeGameViewSize = size => (dispatch, getState) => {
    const {area, player} = getState()

    dispatch({
        type: types.STORE_GAME_VIEW_SIZE,
        size,
        area,
        player,
    })
}

const seekTarget = (missile = false) => (dispatch, getState) => {
    const {factions, fov, player, ui} = getState()
    const prev = ui.game.target

    const targets = seekTargets(player, factions, fov.cumulative, missile)

    if (targets.length > 0) {
        const prevTarget = prev ? targets.find(target => target.id === prev) : undefined

        if (prevTarget) {
            setReticule(prevTarget.location, missile)(dispatch, getState)
        } else {
            setReticule(targets[0].location, missile)(dispatch, getState)
        }
    } else {
        setReticule(player.creature.location, missile)(dispatch, getState)
    }
}

const changeMode = mode => dispatch => dispatch({
    type: types.CHANGE_GAME_MODE,
    mode,
})
