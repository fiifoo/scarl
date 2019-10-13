import { List, Map, Record } from 'immutable'
import * as types from './actionTypes'
import { sendDebugFovQuery, sendDebugWaypointQuery } from './connectionActions'
import * as modes from '../debug/modes'

export const cancelMode = () => dispatch => changeMode(null)(dispatch)

export const debugReceiveMessage = type => (dispatch, getState) => {
    const mode = getState().ui.debug.mode

    switch (type) {
        case 'GameUpdate': {
            switch (mode) {
                case modes.FOV: {
                    sendDebugFovQuery()
                    break
                }
                case modes.PARTY: {
                    setTimeout(() => calculateDebugParty()(dispatch, getState), 0)
                    break
                }
            }
            break
        }
    }
}

export const debugFov = () => dispatch => {
    sendDebugFovQuery()
    changeMode(modes.FOV)(dispatch)
}

export const debugParty = () => (dispatch, getState) => {
    calculateDebugParty()(dispatch, getState)
    changeMode(modes.PARTY)(dispatch)
}

export const debugWaypoint = () => dispatch => {
    sendDebugWaypointQuery()
    changeMode(modes.WAYPOINT)(dispatch)
}

const changeMode = mode => dispatch => dispatch({
    type: types.CHANGE_DEBUG_MODE,
    mode,
})

const Party = Record({
    leader: undefined,
    leaderLocation: null,
    memberLocations: List(),
})

const calculateDebugParty = () => (dispatch, getState) => {
    const {fov} = getState()

    let parties = Map()

    const add = (x, y, leader, isLeader) => {
        const location = {x, y}
        const initial = parties.get(leader, Party({leader}))

        const party = isLeader ? (
            initial.set('leaderLocation', location)
        ) : (
            initial.set('memberLocations', initial.memberLocations.push(location))
        )

        parties = parties.set(party.leader, party)
    }

    fov.cumulative.forEach((fov, x) => fov.forEach((content, y) => {
        content.creatures.forEach(creature => add(x, y, creature.party.leader, creature.id === creature.party.leader))
    }))

    dispatch({
        type: types.RECEIVE_DEBUG_PARTY,
        data: parties,
    })
}
