import * as modes from '../game/modes'
import { seekInteractions } from '../game/interaction'
import { calculateTrajectory, getMissileLauncherRange, getRangedAttackRange, seekTargets } from '../game/utils'
import * as types from './actionTypes'
import { sendAction, sendSignalMapQuery, sendSetEquipmentSet, sendSetQuickItem } from './connectionActions'
import { useInventoryItem } from './playerActions'

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

export const autoMove = () => dispatch => {
    changeMode(modes.AUTO_MOVE)(dispatch)
}

export const crafting = () => dispatch => {
    changeMode(modes.CRAFTING)(dispatch)
}

export const cancelMode = () => dispatch => changeMode(modes.MAIN)(dispatch)

export const doAction = (type, data = {}) => (dispatch, getState) => {
    sendAction(type, data)

    if (isUseScannerAction(getState, type, data)) {
        changeMode(modes.SIGNAL_MAP)(dispatch)
    }
}

export const gameOverScreen = () => dispatch => {
    changeMode(modes.GAME_OVER_SCREEN)(dispatch)
}

export const inventory = () => dispatch => {
    changeMode(modes.INVENTORY)(dispatch)
}

export const interact = (actions = undefined) => (dispatch, getState) => {
    const {player, fov} = getState()

    const interactions = seekInteractions(player, fov.cumulative, actions)

    if (interactions.isEmpty()) {
        addMessage('Nothing to interact with.')(dispatch)
    } else if (actions && interactions.size === 1) {
        const interaction = interactions.first()

        doAction(interaction.action, interaction.data)(dispatch, getState)
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

export const look = (location = undefined) => (dispatch, getState) => {
    const {player} = getState()

    changeMode(modes.LOOK)(dispatch)
    setCursor(location || player.creature.location)(dispatch)
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

export const playerInfo = () => dispatch => changeMode(modes.PLAYER_INFO)(dispatch)

export const previousInteraction = () => (dispatch, getState) => {
    const {interaction, interactions} = getState().ui.game

    const previous = interaction <= 0 ? interactions.size - 1 : interaction - 1
    const location = interactions.get(previous).location

    setCursor(location)(dispatch)
    dispatch({
        type: types.SET_INTERACTION,
        interaction: previous,
    })
}

export const selectCurrentInteraction = () => (dispatch, getState) => {
    const {interaction} = getState().ui.game

    selectInteraction(interaction)(dispatch, getState)
}

export const selectInteraction = key => (dispatch, getState) => {
    const {interactions} = getState().ui.game
    const values = interactions.get(key)

    cancelMode()(dispatch)
    doAction(values.action, values.data)(dispatch, getState)
}

export const setCursor = cursor => dispatch => dispatch({
    type: types.SET_CURSOR,
    cursor,
})

export const setEquipmentSet = set => () => sendSetEquipmentSet(set)

export const setMissile = missile => dispatch => dispatch({
    type: types.SET_MISSILE,
    missile,
})

export const setQuickItem = (slot, item) => () => sendSetQuickItem(slot, item)

export const signalMap = () => (dispatch, getState) => {
    if (getState().player.signals === null) {
        sendSignalMapQuery()
    }

    changeMode(modes.SIGNAL_MAP)(dispatch)
}

export const useQuickItem = slot => (dispatch, getState) => {
    const {settings, player} = getState()
    const inventory = player.inventory

    const kind = settings.quickItems.get(slot)
    if (! kind) {
        return
    }

    const item = inventory.find(x => x.kind === kind)
    if (item) {
        useInventoryItem(item.id)(dispatch, getState)
    }
}

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

export const stance = () => (dispatch) => {
    changeMode(modes.STANCE)(dispatch)
}

export const storeGameViewSize = size => (dispatch, getState) => {
    const {area, player} = getState()

    dispatch({
        type: types.STORE_GAME_VIEW_SIZE,
        size,
        area,
        player,
    })
}

export const world = () => dispatch => changeMode(modes.WORLD)(dispatch)

const seekTarget = (missile = false) => (dispatch, getState) => {
    const {area, factions, fov, player, ui} = getState()
    const prev = ui.game.target

    const targets = seekTargets(player, factions, area, fov.cumulative, missile)

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

const isUseScannerAction = (getState, type, data) => {
    if (type === 'UseItem') {
        const item = getState().player.inventory.get(data.target)

        return item && item.usable && item.usable.type === 'ScanPower'
    } else {
        return false
    }
}
