import { sendWorldAction } from './connectionActions'

export const disembark = to => () => {
    sendWorldAction('Disembark', {to})
}

export const embark = transport => () => {
    sendWorldAction('Embark', {transport})
}

export const travel = to => () => {
    sendWorldAction('Travel', {to})
}
