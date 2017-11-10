/* eslint no-console: 0 */

const HTTP_PING_INTERVAL = 600000 // 10min
const WEBSOCKET_PING_INTERVAL = 30000 // 30s
const MAX_IDLE_TIME = 1800000 // 30min

const createKeepAlive = websocket => {
    let started = false
    let httpId = null
    let websocketId = null
    let latest = Date.now()

    return {
        start: () => {
            started = true
            httpId = setInterval(() => {
                if (Date.now() - latest < MAX_IDLE_TIME) {
                    window.fetch('ping')
                }
            }, HTTP_PING_INTERVAL)
            websocketId = setInterval(() => {
                if (Date.now() - latest < MAX_IDLE_TIME) {
                    websocket.send(null)
                }
            }, WEBSOCKET_PING_INTERVAL)
        },
        clear: () => {
            if (started) {
                started = false
                clearInterval(httpId)
                clearInterval(websocketId)
            }
        },
        refresh: () => {
            latest = Date.now()
        },
    }
}

export default (url, {onOpen, onMessage, onClose}) => {
    const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws'

    const websocket = new WebSocket(`${protocol}://${url}`)
    const keepAlive = createKeepAlive(websocket)

    websocket.onopen = () => {
        console.log('Connection open')
        onOpen()

        keepAlive.start()
    }
    websocket.onmessage = event => {
        const json = JSON.parse(event.data)
        onMessage(json)

        keepAlive.refresh()
    }
    websocket.onclose = () => {
        console.log('Connection closed')
        onClose()

        keepAlive.clear()
    }
    websocket.onerror = () => {
        console.log('Connection error')

        keepAlive.clear()
    }

    return {
        send: data => websocket.send(JSON.stringify(data)),
        close: () => websocket.close(),
    }
}
