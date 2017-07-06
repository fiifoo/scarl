import { Record, Map } from 'immutable'
import * as types from '../actions/actionTypes'

const Debug = Record({
    fov: null,
    waypoint: null,
})

const buildMap = data => Map(data.map(x => [x.key, x.value]))

const buildWaypointNetwork = data => ({
    adjacent: buildMap(data.adjacent),
    areas: buildMap(data.areas),
    locations: buildMap(data.locations),
    waypoints: data.waypoints,
})

export default (state = Debug(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Debug()
        }
        case types.RECEIVE_DEBUG_FOV: {
            return state.set('fov', action.data)
        }
        case types.RECEIVE_DEBUG_WAYPOINT: {
            return state.set('waypoint', {
                network: buildWaypointNetwork(action.data.network),
            })
        }
        default: {
            return state
        }
    }
}
