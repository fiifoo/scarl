import * as commands from '../keyboard/commands'

const directionModifiers = {
    [commands.DIRECTION_SOUTH_WEST]: {x: -1, y: 1},
    [commands.DIRECTION_SOUTH]: {x: 0, y: 1},
    [commands.DIRECTION_SOUTH_EAST]: {x: 1, y: 1},
    [commands.DIRECTION_WEST]: {x: -1, y: 0},
    [commands.DIRECTION_EAST]: {x: 1, y: 0},
    [commands.DIRECTION_NORTH_WEST]: {x: -1, y: -1},
    [commands.DIRECTION_NORTH]: {x: 0, y: -1},
    [commands.DIRECTION_NORTH_EAST]: {x: 1, y: -1},
}

export const isDirectionCommand = command => command.match(/^DIRECTION_/) !== null

export const getDirectionLocation = (command, {x, y}) => {
    const modifier = directionModifiers[command]

    return {
        x: x + modifier.x,
        y: y + modifier.y,
    }
}
