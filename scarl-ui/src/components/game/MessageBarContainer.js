import { List } from 'immutable'
import { connect } from 'react-redux'
import * as modes from '../../game/modes.js'
import { getLocationDescriptions } from '../../game/utils.js'
import MessageBar from './MessageBar.jsx'

const autoMoveMessage = List(['Move: select direction or explore'])

const locationDescriptions = (state, location) => {
    const fov = state.fov.cumulative
    const map = state.area.map
    const kinds = state.kinds

    return getLocationDescriptions(location, fov, map, kinds)
}

const eventMessages = state => (
    state.events.latest.filter(e => e.data.message !== undefined).map(e => e.data.message)
)

const getMessages = state => {
    const mode = state.ui.game.mode

    switch (mode) {
        case modes.AIM:
        case modes.AIM_MISSILE: {
            return locationDescriptions(state, state.ui.game.reticule)
        }
        case modes.AUTO_MOVE: {
            return autoMoveMessage
        }
        case modes.LOOK: {
            return locationDescriptions(state, state.ui.game.cursor)
        }
        default: {
            return eventMessages(state)
        }

    }
}

const MessageBarContainer = connect(
    state => ({
        messages: getMessages(state),
    })
)(MessageBar)

export default MessageBarContainer
