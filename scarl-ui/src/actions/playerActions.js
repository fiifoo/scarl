import * as utils from '../game/utils'
import { sendAction, sendInventoryQuery } from './connectionActions'
import { addMessage, cancelMode, setTarget } from './gameActions'
import { focusKeyboard } from './keyboard'

export const attack = target => (dispatch, getState) => {
    const {player} = getState()

    const shortage = utils.getShortage(player, 'melee')

    if (shortage) {
        addShortageMessage(shortage)(dispatch)
    } else {
        sendAction('Attack', {target})
    }
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
    const {fov, player} = getState()
    const targets = utils.getLocationCreatures(location, fov.cumulative)

    if (targets.length > 0) {
        const target = targets[0]
        setTarget(target.id)(dispatch)
    }

    const shortage = utils.getShortage(player, missile ? 'launcher' : 'ranged')

    cancelMode()(dispatch)

    if (shortage) {
        addShortageMessage(shortage)(dispatch)
    } else {
        const action = missile ? 'ShootMissile' : 'Shoot'

        sendAction(action, {location})
    }
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
