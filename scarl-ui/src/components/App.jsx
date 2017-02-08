import React from 'react'
import ConnectionContainer from '../containers/ConnectionContainer'
import KeyboardContainer from '../containers/KeyboardContainer'
import MessageBarContainer from '../containers/MessageBarContainer'
import MessageLogContainer from '../containers/MessageLogContainer'
import ViewContainer from '../containers/ViewContainer'

import './App.css'

const App = () => (
    <div>
        <KeyboardContainer />

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
            <MessageBarContainer />
            <ViewContainer />
            <MessageLogContainer />
        </div>
    </div>
)

export default App
