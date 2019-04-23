import * as commands from '../../keyboard/commands'
import * as communicateActions from '../communicateActions'

export default (command, dispatch, getState) => {
    switch (command) {
        case commands.END_COMMUNICATION: {
            communicateActions.endCommunication()(dispatch, getState)
            break
        }
    }
}
