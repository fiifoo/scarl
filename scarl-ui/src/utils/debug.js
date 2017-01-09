/* eslint no-console: 0 */

export const log = message => {
    const time = new Date()
    console.log(message + ': ' + time.getSeconds() + '.' + time.getMilliseconds())
}
