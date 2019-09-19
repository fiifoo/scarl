import * as commands from '../../keyboard/commands'
import { getSelectValue, isSelectCommand } from '../../keyboard/utils'
import * as communicateActions from '../communicateActions'


export default (command, dispatch, getState) => {
    switch (command) {
        case commands.END_COMMUNICATION: {
            const event = getCurrentEvent(getState)

            if (event && event.data.choices.length > 0 && ! event.data.interrupted) {
                communicateActions.endConversation()(dispatch, getState)
            } else {
                communicateActions.endCommunication()(dispatch, getState)
            }
            break
        }
        case commands.SELECT_DEFAULT_OR_END_COMMUNICATION: {
            const event = getCurrentEvent(getState)

            if (event && event.data.choices.length > 0 && ! event.data.interrupted) {
                select(1, dispatch, getState)
            } else {
                communicateActions.endCommunication()(dispatch, getState)
            }
            break
        }
        default: {
            if (isSelectCommand(command)) {
                const value = getSelectValue(command)

                select(value, dispatch, getState)
            }
        }
    }
}

const select = (value, dispatch, getState) => {
    const event = getCurrentEvent(getState)

    const conversation = getState().player.conversation
    const interrupted = event && event.data.choices.length > 0 && ! conversation

    if (event && ! interrupted && event.data.choices[value - 1]) {
        const choice = event.data.choices[value - 1]

        communicateActions.converse(event.data.source, choice.communication)(dispatch, getState)
    }
}

const getCurrentEvent = getState => getState().ui.communication.events.first()
