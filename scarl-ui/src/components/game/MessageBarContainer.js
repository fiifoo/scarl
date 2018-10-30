import { List } from 'immutable'
import { connect } from 'react-redux'
import * as modes from '../../game/modes.js'
import { getLocationSummary } from '../../game/utils.js'
import MessageBar from './MessageBar.jsx'

const autoMoveMessage = List(['Move: select direction or explore'])

const getReticuleMessages = (state, location) => {
    const factions = state.factions
    const fov = state.fov.cumulative
    const map = state.area.map
    const kinds = state.kinds
    const player = state.player

    const summary = getLocationSummary(factions, fov, map, kinds, player)(location)

    if (summary) {
        return List([summary])
    } else {
        return List()
    }
}

const getEventMessages = state => (
    state.events.latest.filter(e => e.data.message !== undefined).map(e => e.data.message)
)

const getMessages = state => {
    const mode = state.ui.game.mode

    switch (mode) {
        case modes.AIM:
        case modes.AIM_MISSILE: {
            return getReticuleMessages(state, state.ui.game.reticule)
        }
        case modes.AUTO_MOVE: {
            return autoMoveMessage
        }
        default: {
            return getEventMessages(state)
        }

    }
}

const MessageBarContainer = connect(
    state => ({
        messages: getMessages(state),
    })
)(MessageBar)

export default MessageBarContainer
