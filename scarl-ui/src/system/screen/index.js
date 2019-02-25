import { Map } from 'immutable'

import createMain from './layers/main'
import createMouse from './layers/mouse'
import createSimulateFuture from './layers/simulateFuture'
import createSimulateTravel from './layers/simulateTravel'
import createTravelRoute from './layers/travelRoute'
import { clearContext, transformContext } from './utils'

export default (container, spaceships, stellarBodies, actions) => {
    let layers = Map()

    const build = () => {
        layers = Map({
            main: createMain(spaceships, stellarBodies),
            simulateFuture: createSimulateFuture(spaceships, stellarBodies),
            travelRoute: createTravelRoute(spaceships),
            simulateTravel: createSimulateTravel(spaceships, stellarBodies, actions.clearTravel),
            mouse: createMouse(actions.setSystemViewCenter, actions.setSystemViewScale),
        })

        layers.forEach(x => container.appendChild(x.canvas))
    }

    const update = (world, ui, viewSize) => {
        layers.map(x => x.context).forEach(transformContext(ui.systemView, viewSize))

        layers.get('main').update(world, ui)
        layers.get('simulateFuture').update(world, ui)
        layers.get('travelRoute').update(world, ui)
        layers.get('mouse').update(world, ui, viewSize)

        clear(layers.get('simulateTravel'))
    }

    const simulateTravel = (world, ui) => {
        clear(layers.get('main'))

        layers.get('travelRoute').update(world, ui)
        layers.get('simulateTravel').update(world, ui)
    }

    const clear = layer => clearContext(layer.context)

    return {
        build,
        update,
        simulateTravel,
    }
}
