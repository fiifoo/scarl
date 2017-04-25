import { getAdjacentLocations, getLocationCreature } from '../game/utils'
import { sendMessage } from './connectionActions'

export const attack = target => dispatch => {
    sendMessage({
        type: 'Attack',
        data: {target},
    })(dispatch)
}

export const communicate = () => (dispatch, getState) => {
    const {player, fov} = getState()
    const target = findCommunicateTarget(player, fov)

    if (target) {
        sendMessage({
            type: 'Communicate',
            data: {target},
        })(dispatch)
    }
}

export const equipItem = (item, slot) => dispatch => {
    sendMessage({
        type: 'EquipItem',
        data: {item, slot},
    })(dispatch)
}

export const move = location => dispatch => {
    sendMessage({
        type: 'Move',
        data: {location},
    })(dispatch)
}

export const pass = () => dispatch => {
    sendMessage({
        type: 'Pass',
        data: {},
    })(dispatch)
}

export const pickItem = item => dispatch => {
    sendMessage({
        type: 'PickItem',
        data: {item},
    })(dispatch)
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
