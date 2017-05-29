import createCursor from './layers/cursor'
import createEvent from './layers/event'
import createHighlight from './layers/highlight'
import createMain from './layers/main'

export default (container, kinds) => {
    const layers = {}

    const build = map => {
        layers.cursor = createCursor()
        layers.event = createEvent()
        layers.highlight = createHighlight()
        layers.main = createMain(kinds)

        layers.main.updateMap(map)

        container.appendChild(layers.highlight.canvas)
        container.appendChild(layers.main.canvas)
        container.appendChild(layers.cursor.canvas)
        container.appendChild(layers.event.canvas)
    }

    const reset = map => {
        layers.cursor.clear()
        layers.event.clear()
        layers.highlight.clear()
        layers.main.clear()

        layers.main.updateMap(map)
    }

    const update = (fov, events) => {
        layers.main.update(fov)
        layers.event.update(events)
    }

    const updateCursor = cursor => layers.cursor.update(cursor)

    const updateReticule = (reticule, trajectory) => {
        layers.cursor.update(reticule)
        layers.highlight.update(trajectory)
    }

    return {
        build,
        reset,
        update,
        updateCursor,
        updateReticule,
    }
}
