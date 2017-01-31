import * as types from './actionTypes'
import * as game from './gameActions'

const PASS = 101
const DIRECTIONS = [97, 98, 99, 100, 102, 103, 104, 105]
const DIRECTION_OFFSET = 96

/*
789
4 6
123
*/
const isEast = d => d === 1 || d === 4 || d === 7
const isNorth = d => d === 7 || d === 8 || d === 9
const isSouth = d => d === 1 || d === 2 || d === 3
const isWest = d => d === 3 || d === 6 || d === 9

export const focusKeyboard = () => ({
    type: types.FOCUS_KEYBOARD,
})

export const blurKeyboard = () => ({
    type: types.BLUR_KEYBOARD,
})

export const keypress = event => (dispatch, getState) => {
    const { fov, player } = getState()
    const code = event.which

    if (DIRECTIONS.indexOf(code) !== -1) {
        directionAction(code - DIRECTION_OFFSET, player, fov, dispatch)
    } else {
        switch (code) {
            case PASS: {
                game.pass()(dispatch)
                break
            }
        }
    }

    dispatch({
        type: types.KEYPRESS,
        event,
    })
}

const directionAction = (direction, player, fov, dispatch) => {
    const to = getDirectionLocation(direction, player.location)
    const enemy = getLocationEnemy(to, fov.cumulative)
    if (enemy) {
        game.attack(enemy.id)(dispatch)
    } else {
        game.move(to)(dispatch)
    }
}

const getDirectionLocation = (d, {x, y}) => ({
    x: isEast(d) ? x - 1 : (isWest(d) ? x + 1 : x),
    y: isNorth(d) ? y - 1 : (isSouth(d) ? y + 1 : y),
})

const getLocationEnemy = (l, f) => f[l.x] && f[l.x][l.y] ? f[l.x][l.y].creature : undefined
