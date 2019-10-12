export const getValue = event => event.target.value === '' ? null : event.target.value

export const normalizeValue = value => value === undefined || value === null ? '' : value

export const reactSelect = {
    styles: {menu: (styles) => ({
        ...styles,
        zIndex: 9,
    })},
    filterOption: (candidate, input) => {
        if (! input) {
            return true
        }

        const tokens = input.trim().toLowerCase().split(' ')

        return tokens.filter(token => (
            ! candidate.label.toLowerCase().match(token)
        )).length === 0
    }
}
