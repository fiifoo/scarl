import * as utils from '../game/utils'
import { sendAction, sendInventoryQuery } from './connectionActions'
import { addMessage, cancelMode, setTarget } from './gameActions'
import { getMissileLauncherEquipped } from '../game/utils'

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
    const items = utils.getLocationPickableItems(location, fov.cumulative)

    if (items.length > 0) {
        const item = items[0].id

        sendAction('PickItem', {item})
    } else {
        addMessage('Nothing to pick up.')(dispatch)
    }
}

export const shoot = location => (dispatch, getState) => {
    const {fov, player} = getState()
    const target = utils.getLocationCreature(location, fov.cumulative)

    if (target) {
        setTarget(target.id)(dispatch)
    }

    const action = getMissileLauncherEquipped(player) ? 'ShootMissile' : 'Shoot'

    cancelMode()(dispatch)
    sendAction(action, {location})
}

export const unequipItem = item => () => {
    sendAction('UnequipItem', {item})
    sendInventoryQuery()
}

export const use = () => (dispatch, getState) => {
    const {player, fov} = getState()
    const creature = findUsableCreature(player, fov)

    if (creature) {
        useCreature(creature)()
    } else {
        const item = findUsableItem(player, fov)
        if (item) {
            useItem(item)()
        } else {
            addMessage('Nothing to use here.')(dispatch)
        }
    }
}

export const useCreature = target => () => {
    sendAction('UseCreature', {target})
}

export const useDoor = (door = undefined) => (dispatch, getState) => {
    const {player, fov} = getState()
    const target = door ? door : findDoor(player, fov)

    if (target) {
        sendAction('UseDoor', {target})
    } else {
        addMessage('No doors here.')(dispatch)
    }
}

export const useItem = target => () => {
    sendAction('UseItem', {target})
}

export const useInventoryItem = target => () => {
    useItem(target)()
    sendInventoryQuery()
}

const createAdjacentTargetFinder = getTarget => (player, fov) => {
    let target = undefined
    utils.getAdjacentLocations(player.creature.location).forEach(location => {
        if (target === undefined) {
            target = getTarget(location, fov.cumulative)
        }
    })

    return target !== undefined ? target.id : undefined
}

const createHereOrAdjacentTargetFinder = getTarget => {
    const findAdjacent = createAdjacentTargetFinder(getTarget)

    return (player, fov) => {
        const here = getTarget(player.creature.location, fov.cumulative)

        return here ? here.id : findAdjacent(player, fov)
    }
}

const findCommunicateTarget = createAdjacentTargetFinder(utils.getLocationCreature)

const findDoor = createAdjacentTargetFinder(utils.getLocationDoor)

const findUsableCreature = createAdjacentTargetFinder(utils.getLocationUsableCreature)

const findUsableItem = createHereOrAdjacentTargetFinder(utils.getLocationUsableItem)
