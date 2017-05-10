/* eslint no-console: 0 */

export default (url, {onOpen, onMessage, onClose}) => {
    const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws'

    const websocket = new WebSocket(`${protocol}://${url}`)

    websocket.onopen = () => {
        console.log('Connection open')
        onOpen()
    }
    websocket.onmessage = event => {
        const json = JSON.parse(event.data)
        onMessage(json)
    }
    websocket.onclose = () => {
        console.log('Connection closed')
        onClose()
    }
    websocket.onerror = () => {
        console.log('Connection error')
    }

    return {
        send: data => websocket.send(JSON.stringify(data)),
        close: () => websocket.close(),
    }
}
