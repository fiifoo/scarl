import * as types from '../actions/actionTypes'

const build = (data, fov = []) => {
    data.forEach(item => {
        const x = item.key.x
        const y = item.key.y

        if (fov[x] === undefined) {
            fov[x] = []
        }
        fov[x][y] = item.value
    })

    return fov
}

const buildCumulative = (data, cumulative) => {
    build(data.delta, cumulative)

    data.shouldHide.forEach(l => {
        cumulative[l.x][l.y].creature = undefined
    })

    return cumulative
}

const initial = {
    cumulative: [],
    delta: [],
    shouldHide: [],
}

export default (state = initial, action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return initial
        }
        case types.RECEIVE_MESSAGE: {
            const data = action.data.fov
            return {
                cumulative: buildCumulative(data, state.cumulative), // mutates state!
                delta: build(data.delta),
                shouldHide: data.shouldHide,
            }
        }
        default: {
            return state
        }
    }
}
