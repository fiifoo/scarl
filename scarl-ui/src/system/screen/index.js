import createMain from './layers/main'
import createSimulateFuture from './layers/simulateFuture'
import createSimulateTravel from './layers/simulateTravel'
import createTravelRoute from './layers/travelRoute'

export default (container, spaceships, stellarBodies, clearTravel) => {
    const layers = {}

    const build = () => {
        layers.main = createMain(spaceships, stellarBodies)
        layers.simulateFuture = createSimulateFuture(spaceships, stellarBodies)
        layers.travelRoute = createTravelRoute(spaceships)
        layers.simulateTravel = createSimulateTravel(spaceships, stellarBodies, clearTravel)

        container.appendChild(layers.main.canvas)
        container.appendChild(layers.simulateFuture.canvas)
        container.appendChild(layers.travelRoute.canvas)
        container.appendChild(layers.simulateTravel.canvas)
    }

    const update = (system, ui) => {
        layers.main.update(system)
        layers.simulateFuture.update(system)
        layers.travelRoute.update(system, ui)
        layers.simulateTravel.clear()
    }

    const simulateTravel = (system, ui) => {
        layers.main.clear()
        layers.simulateTravel.update(system, ui)
    }

    return {
        build,
        update,
        simulateTravel,
    }
}
