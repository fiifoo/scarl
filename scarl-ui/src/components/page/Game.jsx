import React from 'react'
import GameViewsContainer from '../game/GameViewsContainer'
import KeyboardContainer from '../game/KeyboardContainer'
import MenuContainer from '../game/MenuContainer'
import MessageBarContainer from '../game/MessageBarContainer'
import StatusBarContainer from '../game/StatusBarContainer'

const Game = ({focusKeyboard}) => (
    <div onClick={focusKeyboard}>
        <KeyboardContainer />
        <div className="container-fluid">
            <div style={{marginTop: 6, marginBottom: 6}}>
                <StatusBarContainer />
            </div>
            <div style={{marginBottom: 6}}>
                <MessageBarContainer />
            </div>
            <div>
                <GameViewsContainer />
            </div>
        </div>
        <MenuContainer />
    </div>
)

export default Game
