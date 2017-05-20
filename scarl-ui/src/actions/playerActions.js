import { getAdjacentLocations, getLocationConduit, getLocationCreature, getLocationPickableItems } from '../game/utils'
import { sendAction, sendInventoryQuery } from './connectionActions'
import { addMessage, cancelMode, setTarget } from './gameActions'

export const attack = target => () => {
    sendAction('Attack', {target})
}

export const communicate = () => (dispatch, getState) => {
    const {player, fov} = getState()
    const target = findCommunicateTarget(player, fov)

    if (target) {
        sendAction('Communicate', {target})
    } else {
        addMessage('No one to talk to.')(dispatch)
    }
}

export const enterConduit = () => (dispatch, getState) => {
    const {player, fov} = getState()
    const location = player.creature.location
    const conduit = getLocationConduit(location, fov.cumulative)

    if (conduit) {
        sendAction('EnterConduit', {conduit})
    } else {
        addMessage('No stairs here.')(dispatch)
    }
}

export const equipItem = (item, slot) => () => {
    sendAction('EquipItem', {item, slot})
    sendInventoryQuery()
}

export const move = location => () => {
    sendAction('Move', {location})
}

export const pass = () => () => {
    sendAction('Pass')
}

export const pickItem = () => (dispatch, getState) => {
    const {player, fov} = getState()
    const location = player.creature.location
    const items = getLocationPickableItems(location, fov.cumulative)

    if (items.length > 0) {
        const item = items[0].id

        sendAction('PickItem', {item})
    } else {
        addMessage('Nothing to pick up.')(dispatch)
    }
}

export const shoot = location => (dispatch, getState) => {
    const {fov} = getState()
    const target = getLocationCreature(location, fov.cumulative)

    if (target) {
        setTarget(target.id)(dispatch)
    }

    cancelMode()(dispatch)
    sendAction('Shoot', {location})
}

const findCommunicateTarget = (player, fov) => {
    let target = undefined
    getAdjacentLocations(player.creature.location).forEach(location => {
        if (target === undefined) {
            target = getLocationCreature(location, fov.cumulative)
        }
    })

    return target !== undefined ? target.id : undefined
}
