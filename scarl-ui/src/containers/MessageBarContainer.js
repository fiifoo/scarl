import { connect } from 'react-redux'
import MessageBar from '../components/MessageBar.jsx'
import * as modes from '../game/modes.js'
import { getLocationDescriptions } from '../game/utils.js'

const locationDescriptions = (state, location) => {
    const fov = state.fov.cumulative
    const map = state.map.locations
    const kinds = state.kinds

    return getLocationDescriptions(location, fov, map, kinds)
}

const getMessages = state => {
    const mode = state.ui.game.mode

    switch (mode) {
        case modes.AIM: {
            return locationDescriptions(state, state.ui.game.reticule)
        }
        case modes.LOOK: {
            return locationDescriptions(state, state.ui.game.cursor)
        }
        default: {
            return state.messages.latest
        }

    }
}

const MessageBarContainer = connect(
    state => ({
        messages: getMessages(state),
    })
)(MessageBar)

export default MessageBarContainer
