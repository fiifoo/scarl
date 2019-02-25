import { sendWorldAction } from './connectionActions'
import * as types from './actionTypes'
import Position from '../system/Position'
import SolarSystem from '../system/SolarSystem'
import { PIXEL_SIZE } from '../system/screen/const'

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

            setTravelSystemView(world.system.bodies.get(fromBody).position, world.system.bodies.get(toBody).position)(dispatch, getState)

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

export const setSystemViewCenter = center => ({
    type: types.SET_SYSTEM_VIEW_CENTER,
    center,
})

export const setSystemViewScale = scale => ({
    type: types.SET_SYSTEM_VIEW_SCALE,
    scale,
})


const setTravelSystemView = (p1, p2) => (dispatch, getState) => {
    const viewSize = getState().ui.game.viewSize

    const center = Position({
        x: (p1.x + p2.x) / 2,
        y: (p1.y + p2.y) / 2,
    })

    const width = Math.abs(p2.x - p1.x)
    const height = Math.abs(p2.y - p1.y)

    const scaleX = viewSize.width * PIXEL_SIZE / width
    const scaleY = viewSize.height * PIXEL_SIZE / height

    const scale = Math.min(1.5, Math.min(scaleX, scaleY) / 2)

    dispatch({
        type: types.SET_SYSTEM_VIEW_CENTER,
        center,
    })
    dispatch({
        type: types.SET_SYSTEM_VIEW_SCALE,
        scale: scale,
    })
}
