import { sendMessage } from './connectionActions'

export const attack = target => dispatch => {
    sendMessage({
        type: 'Attack',
        data: {target},
    })(dispatch)
}

export const communicate = target => dispatch => {
    sendMessage({
        type: 'Communicate',
        data: {target},
    })(dispatch)
}

export const move = location => dispatch => {
    sendMessage({
        type: 'Move',
        data: {location},
    })(dispatch)
}

export const pass = () => dispatch => {
    sendMessage({
        type: 'Pass',
        data: {},
    })(dispatch)
}
