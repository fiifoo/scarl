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

const getInitial = () => ({
    cumulative: [],
    delta: [],
    shouldHide: [],
})

export default (state = getInitial(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return getInitial()
        }
        case types.RECEIVE_AREA_CHANGE: {
            return getInitial()
        }
        case types.RECEIVE_GAME_UPDATE: {
            const data = action.data.fov

            return {
                delta: build(data.delta),
                cumulative: buildCumulative(data, state.cumulative), // mutates state!
                shouldHide: data.shouldHide,
            }
        }
        default: {
            return state
        }
    }
}
