import createCursor from './layers/cursor'
import createDebug from './layers/debug'
import createEvent from './layers/event'
import createHighlight from './layers/highlight'
import createMain from './layers/main'
import createPlayer from './layers/player'

export default (container, kinds) => {
    const layers = {}

    const build = map => {
        layers.cursor = createCursor()
        layers.debug = createDebug()
        layers.event = createEvent()
        layers.highlight = createHighlight()
        layers.main = createMain(kinds)
        layers.player = createPlayer(kinds)

        layers.main.updateMap(map)

        container.appendChild(layers.highlight.canvas)
        container.appendChild(layers.main.canvas)
        container.appendChild(layers.cursor.canvas)
        container.appendChild(layers.event.canvas)
        container.appendChild(layers.debug.canvas)
        container.appendChild(layers.player.canvas)
    }

    const reset = map => {
        layers.cursor.clear()
        layers.debug.clear()
        layers.event.clear()
        layers.highlight.clear()
        layers.main.clear()
        layers.player.clear()

        layers.main.updateMap(map)
    }

    const update = (fov, events, player) => {
        layers.main.update(fov)
        layers.event.update(events)
        layers.player.update(player)
    }

    const updateCursor = cursor => layers.cursor.update(cursor)

    const updateDebug = (mode, debug) => layers.debug.update(mode, debug)

    const updateReticule = (reticule, trajectory) => {
        layers.cursor.update(reticule)
        layers.highlight.update(trajectory)
    }

    return {
        build,
        reset,
        update,
        updateCursor,
        updateDebug,
        updateReticule,
    }
}
