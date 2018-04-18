import React from 'react'
import Header from '../Header.jsx'
import ActionBarContainer from '../game/ActionBarContainer'
import DebugBarContainer from '../game/DebugBarContainer'
import GameViewsContainer from '../game/GameViewsContainer'
import KeyboardContainer from '../game/KeyboardContainer'
import MessageBarContainer from '../game/MessageBarContainer'
import PlayerInfoContainer from '../game/PlayerInfoContainer'

const Spacer = () => <div>&nbsp;</div>

const Game = ({focusKeyboard}) => (
    <div>
        <Header />
        <div onClick={focusKeyboard} className="container-fluid">
            <KeyboardContainer />

            <MessageBarContainer />
            <Spacer />

            <GameViewsContainer />

            <PlayerInfoContainer />
            <Spacer />
            <ActionBarContainer />
            <Spacer />
            <DebugBarContainer />
        </div>
    </div>
)

export default Game
