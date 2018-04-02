import React from 'react'
import ActionBarContainer from '../containers/ActionBarContainer'
import DebugBarContainer from '../containers/DebugBarContainer'
import GameViewsContainer from '../containers/GameViewsContainer'
import KeyboardContainer from '../containers/KeyboardContainer'
import MessageBarContainer from '../containers/MessageBarContainer'
import PlayerInfoContainer from '../containers/PlayerInfoContainer'

const Spacer = () => <div>&nbsp;</div>

const Game = ({focusKeyboard}) => (
    <div onClick={focusKeyboard}>
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
)

export default Game
