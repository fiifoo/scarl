import { Record, Map } from 'immutable'
import * as types from '../actions/actionTypes'

const Debug = Record({
    fov: null,
    party: null,
    waypoint: null,
})

const buildWaypointNetwork = data => ({
    waypoints: data.waypoints,
    adjacentWaypoints: Map(data.adjacentWaypoints),
    locationWaypoint: Map(data.locationWaypoint),
    waypointLocations: Map(data.waypointLocations),
})

export default (state = Debug(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return Debug()
        }
        case types.RECEIVE_DEBUG_FOV: {
            return state.set('fov', action.data)
        }
        case types.RECEIVE_DEBUG_PARTY: {
            return state.set('party', action.data)
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
