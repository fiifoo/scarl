import React from 'react'

const ItemAdd = ({model, id, invalid, copyId, addItem, setId}) => {

    const submit = event => {
        event.preventDefault()
        addItem(model, id)

        return false
    }

    return (
        <form onSubmit={submit} className={invalid ? 'input-group has-error' : 'input-group'}>
            <span className="input-group-btn">
                <button
                    type="button"
                    className="btn btn-success"
                    disabled={! id || ! copyId}
                    onClick={() => addItem(model, id, copyId)}>Copy</button>
            </span>
            <input
                type="text"
                className="form-control"
                value={id || ''}
                placeholder="New item id"
                onChange={event => setId(event.target.value)} />
            <span className="input-group-btn">
                <button
                    type="submit"
                    className="btn btn-success"
                    disabled={! id}>Add</button>
            </span>
        </form>
    )
}

export default ItemAdd
