import React from 'react'
import ConnectionContainer from './ConnectionContainer'

const Header = () => (
    <nav className="navbar navbar-inverse">
        <div className="container-fluid">
            <div className="navbar-header">
                <div className="navbar-brand">Scarl</div>
            </div>
            <div>
                <ConnectionContainer />
            </div>
        </div>
    </nav>
)

export default Header
