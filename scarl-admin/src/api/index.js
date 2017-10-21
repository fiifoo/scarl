const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
}
export const fetchSummary = () => window.fetch('admin/summary', {
    headers,
    method: 'GET',
}).then(response => {
    if (response.ok) {
        return response.json()
    } else {
        throw Error('Fetching summary failed')
    }
})

export const save = data => window.fetch('admin', {
    headers,
    method: 'PUT',
    body: JSON.stringify(data),
})
