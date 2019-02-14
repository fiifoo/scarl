import { sendWorldAction } from './connectionActions'
import * as types from './actionTypes'
import SolarSystem from '../system/SolarSystem'

export const disembark = to => dispatch => {
    dispatch(clearTravel())

    sendWorldAction('Disembark', {to})
}

export const embark = transport => () => {
    sendWorldAction('Embark', {transport})
}

export const calculateTravel = to => (dispatch, getState) => {
    const world = getState().world

    const transport = world.transports.find(x => x.hub === world.site)
    const from = world.transportRegions.get(transport.id)

    const ship = transport.spaceship
    const fromBody = world.regions.get(from).stellarBody
    const toBody = world.regions.get(to).stellarBody

    if (ship && fromBody != toBody) {
        const travel = SolarSystem.calculateTravel(ship, toBody)(world.system)

        if (travel) {
            dispatch({
                type: types.SET_TRAVEL,
                to,
                possible: true,
                ship,
                travel,
            })
        } else {
            dispatch({
                type: types.SET_TRAVEL,
                to,
                possible: false,
            })
        }
    } else {
        dispatch({
            type: types.SET_TRAVEL,
            to,
            possible: true,
        })
    }
}

export const travel = () => (dispatch, getState) => {
    const travel = getState().ui.world.travel

    if (! travel) {
        return
    }

    if (travel.ship) {
        dispatch({
            type: types.SIMULATE_TRAVEL,
        })
    }

    sendWorldAction('Travel', {to: travel.to})
}

export const clearTravel = () => ({
    type: types.CLEAR_TRAVEL,
})
