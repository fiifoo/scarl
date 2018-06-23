import React from 'react'
import MenuItem from './MenuItem.jsx'

const Connection = ({closeConnection, closingConnection, connection, openConnection, openingConnection}) => (
    connection ? (
        <MenuItem
            autoFocus
            onClick={closeConnection}
            disabled={closingConnection}
            label="Quit game" />
    ) : (
        <MenuItem
            autoFocus
            onClick={openConnection}
            disabled={openingConnection}
            label="Start game" />
    )
)

export default Connection
