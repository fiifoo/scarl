/* eslint no-console: 0 */

const PING_INTERVAL = 30000 // 30s
const MAX_IDLE_TIME = 1800000 // 30min

const createKeepAlive = websocket => {
    let id = null
    let latest = Date.now()

    return {
        start: () => id = setInterval(() => {
            if (Date.now() - latest < MAX_IDLE_TIME) {
                websocket.send(null)
            }
        }, PING_INTERVAL),
        refresh: () => latest = Date.now(),
        clear: () => id && clearInterval(id),
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
