import * as bindings from '../keyboard/bindings'
import * as commands from '../keyboard/commands'
import * as types from './actionTypes'
import * as game from './gameActions'

export const focusKeyboard = () => ({
    type: types.FOCUS_KEYBOARD,
})

export const blurKeyboard = () => ({
    type: types.BLUR_KEYBOARD,
})

export const keypress = event => (dispatch, getState) => {
    const code = event.which
    const command = bindings.main.get(code)

    if (command === undefined) {
        return
    }

    const { fov, player } = getState()

    if (bindings.directions.has(code)) {
        directionAction(command, player, fov, dispatch)

        return
    }

    switch (command) {
        case commands.COMMUNICATE: {
            communicateAction(player, fov, dispatch)
            break
        }
        case commands.PASS: {
            game.pass()(dispatch)
            break
        }
    }
}

const directionAction = (command, player, fov, dispatch) => {
    const to = getDirectionLocation(command, player.location)
    const target = getLocationCreature(to, fov.cumulative)

    if (target) {
        game.attack(target.id)(dispatch)
    } else {
        game.move(to)(dispatch)
    }
}

const communicateAction = (player, fov, dispatch) => {
    let target = undefined
    getAdjacentLocations(player.location).forEach(location => {
        if (target === undefined) {
            target = getLocationCreature(location, fov.cumulative)
        }
    })

    if (target) {
        game.communicate(target.id)(dispatch)
    }
}

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

const getDirectionLocation = (command, {x, y}) => {
    const modifier = directionModifiers[command]

    return {
        x: x + modifier.x,
        y: y + modifier.y,
    }
}

const getLocationCreature = (l, f) => f[l.x] && f[l.x][l.y] ? f[l.x][l.y].creature : undefined

const getAdjacentLocations = l => ([
    {x: l.x - 1, y: l.y - 1},
    {x: l.x - 1, y:  l.y},
    {x: l.x - 1, y:  l.y + 1},
    {x: l.x, y:  l.y + 1},
    {x: l.x + 1, y:  l.y + 1},
    {x: l.x + 1, y:  l.y},
    {x: l.x + 1, y:  l.y - 1},
    {x: l.x, y:  l.y - 1}
])
