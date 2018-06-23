import React from 'react'
import { ESC } from '../../keyboard/keycodes'
import Header from '../Header.jsx'
import ActionBarContainer from './ActionBarContainer'
import DebugBarContainer from './DebugBarContainer'

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

const cancel = (cancelMode, focusKeyboard) => event => {
    if (event.which === ESC) {
        focusKeyboard()
        cancelMode()
    }
}

const Menu = ({cancelMode, focusKeyboard}) => (
    <div style={style} onKeyDown={cancel(cancelMode, focusKeyboard)}>
        <Header />
        <Spacer />
        <ActionBarContainer />
        <Spacer />
        <DebugBarContainer />
    </div>
)

export default Menu
