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

    const update = (world, ui) => {
        layers.main.update(world, ui)
        layers.simulateFuture.update(world, ui)
        layers.travelRoute.update(world, ui)
        layers.simulateTravel.clear()
    }

    const simulateTravel = (world, ui) => {
        layers.main.clear()
        layers.travelRoute.update(world, ui)
        layers.simulateTravel.update(world, ui)
    }

    return {
        build,
        update,
        simulateTravel,
    }
}
