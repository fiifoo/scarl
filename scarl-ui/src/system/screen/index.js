import createMain from './layers/main'
import createSimulateFuture from './layers/simulateFuture'
import createSimulateTravel from './layers/simulateTravel'

export default (container, spaceships, stellarBodies, clearTravelSimulation) => {
    const layers = {}

    const build = () => {
        layers.main = createMain(spaceships, stellarBodies)
        layers.simulateFuture = createSimulateFuture(spaceships, stellarBodies)
        layers.simulateTravel = createSimulateTravel(spaceships, stellarBodies, clearTravelSimulation)

        container.appendChild(layers.main.canvas)
        container.appendChild(layers.simulateFuture.canvas)
        container.appendChild(layers.simulateTravel.canvas)
    }

    const update = system => {
        layers.main.update(system)
        layers.simulateFuture.update(system)
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
