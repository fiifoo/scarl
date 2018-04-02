import { EVENT_DURATION, EXPLOSION_COLOR, HIGHLIGHT_COLOR, SHOT_COLOR } from '../const'
import { clearContext, createCanvas, createDraw } from '../utils'

const filterEvents = e => e.type !== 'GenericEvent'

export default area => {
    const canvas = createCanvas(area)
    const context = canvas.getContext('2d')
    const draw = createDraw(context)

    const clear = () => clearContext(area, context)

    const update = events => {
        events.filter(filterEvents).forEach(renderEvent)

        setTimeout(clear, EVENT_DURATION)
    }

    const renderEvent = event => {
        switch (event.type) {
            case 'ExplosionEvent': {
                renderExplosion(event.data)
                break
            }
            case 'HitEvent': {
                renderHit(event.data)
                break
            }
            case 'MoveMissileEvent': {
                renderMoveMissile(event.data)
                break
            }
            case 'ShotEvent': {
                renderShot(event.data)
                break
            }
        }
    }

    const renderExplosion = ({location}) => {
        draw.fill(EXPLOSION_COLOR)(location)
    }

    const renderHit = ({location}) => {
        draw.fill(HIGHLIGHT_COLOR)(location)
    }

    const renderMoveMissile = ({from, to}) => {
        draw.line(SHOT_COLOR)(from)(to)
    }

    const renderShot = ({from, to}) => {
        draw.line(SHOT_COLOR)(from)(to)
        draw.dot(SHOT_COLOR)(to)
    }

    return {
        canvas,
        update,
    }
}
