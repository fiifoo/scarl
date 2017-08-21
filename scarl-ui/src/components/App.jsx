import React from 'react'
import ConnectionContainer from '../containers/ConnectionContainer'
import CreateGameContainer from '../containers/CreateGameContainer'
import GameContainer from '../containers/GameContainer'

import './App.css'

const App = () => (
    <div>
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

        <div className="container-fluid">
            <CreateGameContainer />
            <GameContainer />
        </div>
    </div>
)

export default App
