import { EVENT_DURATION, HIGHLIGHT_COLOR, SHOT_COLOR, TILE_MIDDLE, TILE_SIZE } from '../const'
import { clearContext, createCanvas } from '../utils'

const filterEvents = e => e.type !== 'GenericEvent'

export default () => {
    const canvas = createCanvas()
    const context = canvas.getContext('2d')

    const clear = () => clearContext(context)

    const update = events => {
        events.filter(filterEvents).forEach(renderEvent)

        setTimeout(clear, EVENT_DURATION)
    }

    const renderEvent = event => {
        switch (event.type) {
            case 'HitEvent': {
                renderHit(event.data)
                break
            }
            case 'ShotEvent': {
                renderShot(event.data)
                break
            }
        }
    }

    const renderHit = ({location}) => {
        const x = location.x * TILE_SIZE
        const y = location.y * TILE_SIZE

        context.fillStyle = HIGHLIGHT_COLOR
        context.fillRect(x, y, TILE_SIZE, TILE_SIZE)
    }

    const renderShot = ({from, to}) => {
        const from_x = from.x * TILE_SIZE + TILE_MIDDLE + 0.5
        const from_y = from.y * TILE_SIZE + TILE_MIDDLE + 0.5
        const to_x = to.x * TILE_SIZE + TILE_MIDDLE + 0.5
        const to_y = to.y * TILE_SIZE + TILE_MIDDLE + 0.5

        context.fillStyle = SHOT_COLOR
        context.strokeStyle = SHOT_COLOR

        context.beginPath()
        context.moveTo(from_x, from_y)
        context.lineTo(to_x, to_y)
        context.stroke()

        context.beginPath()
        context.moveTo(to_x, to_y)
        context.arc(to_x, to_y, 3, 0, Math.PI * 2)
        context.fill()
    }

    return {
        canvas,
        clear,
        update,
    }
}
