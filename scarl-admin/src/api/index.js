const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
}

const NO_CONTENT = 204

const processResponse = errorMessage => response => {
    if (response.ok) {
        if (response.status === NO_CONTENT) {
            return null
        } else {
            return response.json()
        }
    } else {
        throw Error(errorMessage)
    }
}

export const fetchSummary = () => window.fetch('admin/summary', {
    headers,
    method: 'GET',
}).then(processResponse('Fetching summary failed'))

export const save = data => window.fetch('admin', {
    headers,
    method: 'PUT',
    body: JSON.stringify(data),
}).then(processResponse('Saving data failed'))

export const simulate = () => window.fetch('admin/simulate', {
    headers,
    method: 'PUT',
}).then(processResponse('Simulate failed'))

export const saveUi = data => window.fetch('admin/ui', {
    headers,
    method: 'PUT',
    body: JSON.stringify(data),
}).then(processResponse('Saving data failed'))
