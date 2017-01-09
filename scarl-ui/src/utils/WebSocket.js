/* eslint no-console: 0 */

export default (url, {onOpen, onMessage, onClose}) => {

    const websocket = new WebSocket(`ws://${url}`)

    websocket.onopen = () => {
        console.log('Connection open')
        onOpen()
    }
    websocket.onmessage = event => {
        //console.log('Connection message')
        const json = JSON.parse(event.data)
        //console.log(json)
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
