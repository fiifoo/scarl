import * as modes from '../../debug/modes'
import { HIGHLIGHT_COLOR } from '../const'
import { clearContext, createCanvas, createDraw, getRandomColor } from '../utils'

export default area => {
    const canvas = createCanvas(area)
    const context = canvas.getContext('2d')
    const draw = createDraw(context)

    const clear = () => clearContext(area, context)

    const update = (mode, debug) => {
        clear()

        switch (mode) {
            case modes.FOV: {
                if (debug.fov) {
                    renderFov(debug.fov)
                }
                break
            }
            case modes.PARTY: {
                if (debug.party) {
                    renderParties(debug.party)
                }
                break
            }
            case modes.WAYPOINT: {
                if (debug.waypoint) {
                    renderWaypoint(debug.waypoint)
                }
                break
            }
        }
    }

    const renderFov = ({locations}) => {
        locations.forEach(draw.fill(HIGHLIGHT_COLOR))
    }

    const renderParties = parties => {
        parties.map(party => {
            const color = getRandomColor(1)
            const fill = draw.fill(color, 0.5)
            const dot = draw.dot(color)

            if (party.leaderLocation) {
                dot(party.leaderLocation)
                fill(party.leaderLocation)
            }
            party.memberLocations.forEach(fill)
        })
    }

    const renderWaypoint = ({network}) => {
        network.waypoints.forEach(draw.dot('blue'))

        network.adjacentWaypoints.forEach((adjacent, waypoint) => {
            adjacent.forEach(draw.line('blue')(waypoint))
        })

        network.waypointLocations.forEach(locations => {
            locations.forEach(draw.fill(getRandomColor('0.2')))
        })
    }

    return {
        canvas,
        update,
    }
}
