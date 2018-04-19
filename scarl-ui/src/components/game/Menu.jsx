import React from 'react'
import Header from '../Header.jsx'
import ActionBarContainer from '../game/ActionBarContainer'
import DebugBarContainer from '../game/DebugBarContainer'

const style = {
    position: 'fixed',
    width: '100%',
    height: '100%',
    top: 0,
    left: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.7)',
    zIndex: 999,
}

const Spacer = () => <div>&nbsp;</div>

const Menu = () => (
    <div style={style}>
        <Header />
        <div className="container-fluid">
            <ActionBarContainer />
            <Spacer />
            <DebugBarContainer />
        </div>
    </div>
)

export default Menu
