const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
}

export const save = data => window.fetch('admin', {
    headers,
    method: 'PUT',
    body: JSON.stringify(data),
})
