import * as utils from '../game/utils'
import { sendAction, sendInventoryQuery } from './connectionActions'
import { addMessage, cancelMode, setTarget } from './gameActions'
import { focusKeyboard } from './keyboard'

export const attack = target => () => {
    sendAction('Attack', {target})
}

export const displace = target => () => {
    sendAction('Displace', {target})
}

export const dropItem = item => () => {
    sendAction('DropItem', {item})
    sendInventoryQuery()
}

export const enterConduit = () => (dispatch, getState) => {
    const {player, fov} = getState()
    const location = player.creature.location
    const conduit = utils.getLocationConduit(location, fov.cumulative)

    if (conduit) {
        sendAction('EnterConduit', {conduit})
    } else {
        addMessage('No stairs here.')(dispatch)
    }
}

export const equipItem = (item, slot) => dispatch => {
    sendAction('EquipItem', {item, slot})
    sendInventoryQuery()
    setTimeout(() => dispatch(focusKeyboard()), 0) // SplitButton captures focus...
}

export const move = location => () => {
    sendAction('Move', {location})
}

export const pass = () => () => {
    sendAction('Pass')
}

export const shoot = (location, missile = false) => (dispatch, getState) => {
    const {fov} = getState()
    const targets = utils.getLocationCreatures(location, fov.cumulative)

    if (targets.length > 0) {
        const target = targets[0]
        setTarget(target.id)(dispatch)
    }

    const action = missile ? 'ShootMissile' : 'Shoot'

    cancelMode()(dispatch)
    sendAction(action, {location})
}

export const unequipItem = item => () => {
    sendAction('UnequipItem', {item})
    sendInventoryQuery()
}

export const useDoor = target => () => {
    sendAction('UseDoor', {target})
}

export const useInventoryItem = target => () => {
    sendAction('UseItem', {target})
    sendInventoryQuery()
}
