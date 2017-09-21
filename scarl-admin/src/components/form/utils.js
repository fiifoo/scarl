export const getValue = event => event.target.value === '' ? null : event.target.value

export const normalizeValue = value => value === undefined || value === null ? '' : value
