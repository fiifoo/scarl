import { sendWorldAction } from './connectionActions'
import * as types from './actionTypes'

export const disembark = to => () => {
    sendWorldAction('Disembark', {to})
}

export const embark = transport => () => {
    sendWorldAction('Embark', {transport})
}

export const travel = to => (dispatch, getState) => {
    const world = getState().world

    const transport = world.transports.find(x => x.hub === world.site)
    const from = world.transportRegions.get(transport.id)

    if (transport.spaceship && world.regions.get(from).stellarBody != world.regions.get(to).stellarBody) {
        dispatch(setTravelSimulation(transport.spaceship, world.regions.get(to).stellarBody))
    }

    sendWorldAction('Travel', {to})
}

export const clearTravelSimulation = () => ({
    type: types.CLEAR_TRAVEL_SIMULATION,
})

const setTravelSimulation = (shipId, destinationId) => ({
    type: types.SET_TRAVEL_SIMULATION,
    shipId,
    destinationId,
})
