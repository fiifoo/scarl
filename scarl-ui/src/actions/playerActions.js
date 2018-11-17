import * as utils from '../game/utils'
import { sendInventoryQuery } from './connectionActions'
import { addMessage, cancelMode, doAction, setTarget } from './gameActions'
import { focusKeyboard } from './keyboard'

export const attack = target => (dispatch, getState) => {
    const {player} = getState()

    const shortage = utils.getAttackShortage(player, 'melee')

    if (shortage) {
        addShortageMessage(shortage)(dispatch)
    } else {
        doAction('Attack', {target})(dispatch, getState)
    }
}

export const cancelRecycleItem = item => (dispatch, getState) => {
    doAction('CancelRecycleItem', {item})(dispatch, getState)
    sendInventoryQuery()
}

export const displace = target => (dispatch, getState) => {
    doAction('Displace', {target})(dispatch, getState)
}

export const craftItem = (recipe, equip = false) => (dispatch, getState) => {
    doAction('CraftItem', {recipe, equip})(dispatch, getState)
    sendInventoryQuery()
}

export const dropItem = item => (dispatch, getState) => {
    doAction('DropItem', {item})(dispatch, getState)
    sendInventoryQuery()
    recaptureFocus(dispatch)
}

export const enterConduit = () => (dispatch, getState) => {
    const {player, fov} = getState()
    const location = player.creature.location
    const conduit = utils.getLocationConduit(location, fov.cumulative)

    if (conduit) {
        doAction('EnterConduit', {conduit})(dispatch, getState)
    } else {
        addMessage('No stairs here.')(dispatch)
    }
}

export const equipItem = (item, slot) => (dispatch, getState) => {
    doAction('EquipItem', {item, slot})(dispatch, getState)
    sendInventoryQuery()
    recaptureFocus(dispatch)
}

export const move = location => (dispatch, getState) => {
    doAction('Move', {location})(dispatch, getState)
}

export const pass = () => (dispatch, getState) => {
    doAction('Pass')(dispatch, getState)
}

export const recycleInventoryItem = item => (dispatch, getState) => {
    doAction('RecycleItem', {target: item})(dispatch, getState)
    sendInventoryQuery()
    recaptureFocus(dispatch)
}

export const shoot = (location, missile = false) => (dispatch, getState) => {
    const {fov, player} = getState()
    const targets = utils.getLocationCreatures(location, fov.cumulative)

    if (targets.length > 0) {
        const target = targets[0]
        setTarget(target.id)(dispatch)
    }

    const shortage = utils.getAttackShortage(player, missile ? 'launcher' : 'ranged')

    cancelMode()(dispatch)

    if (shortage) {
        addShortageMessage(shortage)(dispatch)
    } else {
        const action = missile ? 'ShootMissile' : 'Shoot'

        doAction(action, {location})(dispatch, getState)
    }
}

export const unequipItem = item => (dispatch, getState) => {
    doAction('UnequipItem', {item})(dispatch, getState)
    sendInventoryQuery()
}

export const useDoor = target => (dispatch, getState) => {
    doAction('UseDoor', {target})(dispatch, getState)
}

export const useInventoryItem = target => (dispatch, getState) => {
    const {inventory, player} = getState()

    const item = inventory.get(target)
    const consumption = getUsableConsumption(item)
    const shortage = consumption ? utils.getShortage(player, consumption) : null

    if (shortage) {
        addShortageMessage(shortage)(dispatch)
    } else {
        doAction('UseItem', {target})(dispatch, getState)
        sendInventoryQuery()
    }
}

const getUsableConsumption = item => {
    if (! item.usable || ! item.usable.data.resources) {
        return null
    }

    return {
        energy: -item.usable.data.resources.energy,
        materials: -item.usable.data.resources.materials,
    }
}

const addShortageMessage = shortage => dispatch => {
    let message

    if (shortage.energy && shortage.materials) {
        message = 'Not enough energy or materials.'
    } else if (shortage.energy) {
        message = 'Not enough energy.'
    } else if (shortage.materials) {
        message = 'Not enough materials.'
    }

    if (message) {
        addMessage(message)(dispatch)
    }
}

const recaptureFocus = dispatch => {
    setTimeout(() => dispatch(focusKeyboard()), 0) // SplitButton captures focus...
}
