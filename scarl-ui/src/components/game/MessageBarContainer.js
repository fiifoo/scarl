import { List } from 'immutable'
import { connect } from 'react-redux'
import { getMissile } from '../../game/utils.js'
import * as modes from '../../game/modes.js'
import { getLocationSummary } from '../../game/utils.js'
import MessageBar from './MessageBar.jsx'

const autoMoveMessage = List(['Move: select direction or explore'])
const signalMapMessage = List(['Detected signals'])

const getAimMessages = state => {
    const mode = state.ui.game.mode
    const location = state.ui.game.reticule

    const factions = state.factions
    const fov = state.fov.cumulative
    const map = state.area.map
    const kinds = state.kinds
    const player = state.player

    const missile = mode === modes.AIM_MISSILE && getMissile(state)

    const action = missile ? `Aim missile (${kinds.creatures.get(missile).name})` : 'Aim'
    const summary = getLocationSummary(factions, fov, map, kinds, player)(location)

    if (summary) {
        return List([`${action}:`, summary])
    } else {
        return List([action])
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
            return getAimMessages(state)
        }
        case modes.AUTO_MOVE: {
            return autoMoveMessage
        }
        case modes.SIGNAL_MAP: {
            return signalMapMessage
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
