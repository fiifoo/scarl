import createCursor from './layers/cursor'
import createDebug from './layers/debug'
import createEvent from './layers/event'
import createHighlight from './layers/highlight'
import createMain from './layers/main'
import createMouse from './layers/mouse'
import createPlayer from './layers/player'
import createSignal from './layers/signal'

export default (container, kinds, autoMove, look, focusKeyboard) => {
    const layers = {}

    const build = area => {
        layers.cursor = createCursor(area)
        layers.debug = createDebug(area)
        layers.event = createEvent(area)
        layers.highlight = createHighlight(area)
        layers.main = createMain(area, kinds)
        layers.mouse = createMouse(area, autoMove, look, focusKeyboard)
        layers.player = createPlayer(area, kinds)
        layers.signal = createSignal(area)

        layers.main.updateMap(area.map)

        container.appendChild(layers.highlight.canvas)
        container.appendChild(layers.main.canvas)
        container.appendChild(layers.cursor.canvas)
        container.appendChild(layers.event.canvas)
        container.appendChild(layers.signal.canvas)
        container.appendChild(layers.debug.canvas)
        container.appendChild(layers.player.canvas)
        container.appendChild(layers.mouse.canvas)
    }

    const reset = area => {
        while (container.firstChild) {
            container.removeChild(container.firstChild)
        }

        build(area)
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

    const updateSignals = signals => layers.signal.update(signals)

    return {
        build,
        reset,
        update,
        updateCursor,
        updateDebug,
        updateReticule,
        updateSignals,
    }
}
