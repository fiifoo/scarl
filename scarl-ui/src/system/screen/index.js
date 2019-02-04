import createMain from './layers/main'

export default (container, spaceships, stellarBodies) => {
    const layers = {}

    const build = () => {
        layers.main = createMain(spaceships, stellarBodies)

        container.appendChild(layers.main.canvas)
    }

    const update = system => {
        layers.main.update(system)
    }

    return {
        build,
        update,
    }
}
