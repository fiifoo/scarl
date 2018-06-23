import React from 'react'

export default ({label, ...props}) => (
    <div className="scarl-menu-item">
        <button
            type="button"
            className="btn btn-link"
            {...props}>
            {label}
        </button>
    </div>
)
