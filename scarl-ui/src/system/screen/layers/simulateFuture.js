import { Record, List } from 'immutable'
import SolarSystem from '../../SolarSystem'
import { clearContext, createCanvas } from '../utils'

const LIMIT = 3600 * 24 * 500
const TICK = 3600 * 24

const NO_MASS = 0.000001

const isTraveler = stellarBodies => body => stellarBodies.get(body.source).category === 'StellarBodySource.BlackHoleCategory' // not true

const State = Record({
    time: undefined,
    bodies: List(),
    ships: List(),
})
State.simulate = stellarBodies => system => {
    const start = system.time

    const bodies = []
    const ships = []

    const writeBody = body => {
        bodies.push({
            source: body.source,
            position: body.position,
        })
    }

    const writeShip = ship => {
        ships.push({
            source: ship.source,
            position: ship.travel.position,
        })
    }

    const travelers = system.bodies.filter(isTraveler(stellarBodies)).map(x => x.id).keySeq().toSet()
    system = system.set('bodies', system.bodies.map(body => (
        travelers.contains(body.id) ? (
            body.set('mass', NO_MASS)
        ) : (
            body
        )
    )))

    while (system.time < start + LIMIT) {
        system = SolarSystem.tick(TICK)(system)

        system.bodies.forEach(writeBody)
        system.ships.filter(x => !! x.travel).forEach(writeShip)
    }

    return State({
        time: start,
        bodies: List(bodies),
        ships: List(ships),
    })
}

export default (spaceships, stellarBodies) => {
    const {canvas, context, draw} = createCanvas()

    let state = State()

    const drawBody = draw.dot(2)
    const drawShip = draw.dot(1)

    const renderBody = ({source, position}) => {
        const color = stellarBodies.get(source).color

        drawBody(color)(position.x, position.y)
    }

    const renderShip = ({source, position}) => {
        const color = stellarBodies.get(source).color

        drawShip(color)(position.x, position.y)
    }

    const clear = () => clearContext(context)

    const update = (world, ui) => {
        clear()

        if (world.system.time !== state.time) {
            state = State.simulate(stellarBodies)(world.system)
        }

        context.globalAlpha = 0.5 * Math.min(1, ui.systemView.scale)

        state.bodies.forEach(renderBody)
        state.ships.forEach(renderShip)
    }

    return {
        canvas,
        context,
        draw,
        update,
    }
}
