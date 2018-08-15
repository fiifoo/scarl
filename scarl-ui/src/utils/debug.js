/* eslint no-console: 0 */

const enabled = true

let previous = null

export const log = message => {
    if (! enabled) {
        return
    }

    const time = new Date()
    const diff = previous ? time - previous : null
    console.log(message + ': ' + time.getSeconds() + '.' + time.getMilliseconds() + (diff ? ', diff: ' + diff : ''))
    previous = time
}
