import * as modes from '../../debug/modes'
import { HIGHLIGHT_COLOR } from '../const'
import { clearContext, createCanvas, createDraw, getRandomColor } from '../utils'

export default () => {
    const canvas = createCanvas()
    const context = canvas.getContext('2d')
    const draw = createDraw(context)

    const clear = () => clearContext(context)

    const update = (mode, debug) => {
        clear()

        switch (mode) {
            case modes.FOV:
                if (debug.fov) {
                    renderFov(debug.fov)
                }
                break
            case modes.WAYPOINT:
                if (debug.waypoint) {
                    renderWaypoint(debug.waypoint)
                }
                break
        }
    }

    const renderFov = ({locations}) => {
        locations.forEach(draw.fill(HIGHLIGHT_COLOR))
    }

    const renderWaypoint = ({network}) => {
        network.waypoints.forEach(draw.dot('blue'))

        network.adjacent.forEach((adjacent, waypoint) => {
            adjacent.forEach(draw.line('blue')(waypoint))
        })

        network.areas.forEach(locations => {
            locations.forEach(draw.fill(getRandomColor('0.2')))
        })
    }

    return {
        canvas,
        clear,
        update,
    }
}