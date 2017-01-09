import React from 'react'

const Connection = ({closeConnection, closingConnection, connection, openConnection, openingConnection, ping}) => (
    <div className="btn-toolbar">
        <button
            type="button"
            className="btn btn-success"
            onClick={openConnection}
            disabled={connection || openingConnection}
            >Connect</button>
        <button
            type="button"
            className="btn btn-danger"
            onClick={closeConnection}
            disabled={! connection || closingConnection}
            >Disconnect</button>
        <button
            type="button"
            className="btn btn-primary"
            onClick={ping}
            disabled={! connection || closingConnection}
            >Ping!</button>
    </div>
)

export default Connection
