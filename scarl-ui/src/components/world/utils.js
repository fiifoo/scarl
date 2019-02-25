export const humanizeDuration = (seconds, asText = true) => {
    const totalHours = Math.floor(seconds / 3600)
    const hours = totalHours % 24
    const days = Math.floor(totalHours / 24)

    const duration = {
        days: days === 0 ? null : (
            days === 1 ? '1 day' : days + ' days'
        ),
        hours: hours === 1 ? '1 hour' : hours + ' hours'
    }

    return ! asText ? duration : (
        duration.days ? duration.days + ' ' + duration.hours : duration.hours
    )
}
