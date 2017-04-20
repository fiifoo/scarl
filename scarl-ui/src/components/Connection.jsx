import React from 'react'

const Connection = ({closeConnection, closingConnection, connection, openConnection, openingConnection}) => (
    <div className="btn-toolbar">
        <button
            type="button"
            className="btn btn-default navbar-btn"
            onClick={openConnection}
            disabled={connection || openingConnection}
            >Connect</button>
        <button
            type="button"
            className="btn btn-default navbar-btn"
            onClick={closeConnection}
            disabled={! connection || closingConnection}
            >Disconnect</button>
    </div>
)

export default Connection
