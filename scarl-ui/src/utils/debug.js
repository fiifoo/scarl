/* eslint no-console: 0 */

let previous = null

export const log = message => {
    const time = new Date()
    const diff = previous ? time - previous : null
    console.log(message + ': ' + time.getSeconds() + '.' + time.getMilliseconds() + (diff ? ', diff: ' + diff : ''))
    previous = time
}
