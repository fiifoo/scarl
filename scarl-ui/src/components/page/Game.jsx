import React from 'react'
import GameViewsContainer from '../game/GameViewsContainer'
import KeyboardContainer from '../game/KeyboardContainer'
import MenuContainer from '../game/MenuContainer'
import MessageBarContainer from '../game/MessageBarContainer'
import PlayerInfoContainer from '../game/PlayerInfoContainer'

const Game = ({focusKeyboard}) => (
    <div onClick={focusKeyboard}>
        <KeyboardContainer />
        <div className="container-fluid">
            <div style={{marginTop: 6, marginBottom: 6}}>
                <PlayerInfoContainer />
            </div>
            <MessageBarContainer />
            <GameViewsContainer />
        </div>
        <MenuContainer />
    </div>
)

export default Game
