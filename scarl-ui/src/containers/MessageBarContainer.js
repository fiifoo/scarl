import { connect } from 'react-redux'
import MessageBar from '../components/MessageBar.jsx'
import * as modes from '../game/modes.js'
import { getLocationDescriptions } from '../game/utils.js'

const getMessages = state => {
    const mode = state.ui.game.mode

    switch (mode) {
        case modes.AIM:
        case modes.LOOK: {
            const location = state.ui.game.cursor
            const fov = state.fov.cumulative
            const map = state.map.locations
            const kinds = state.kinds

            return getLocationDescriptions(location, fov, map, kinds)
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
