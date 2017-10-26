import React from 'react'
import ActionBarContainer from '../containers/ActionBarContainer'
import DebugBarContainer from '../containers/DebugBarContainer'
import GameOverContainer from '../containers/GameOverContainer'
import InventoryContainer from '../containers/InventoryContainer'
import KeyBindingsContainer from '../containers/KeyBindingsContainer'
import KeyboardContainer from '../containers/KeyboardContainer'
import MessageBarContainer from '../containers/MessageBarContainer'
import MessageLogContainer from '../containers/MessageLogContainer'
import PlayerInfoContainer from '../containers/PlayerInfoContainer'
import ScreenContainer from '../containers/ScreenContainer'

const Spacer = () => <div>&nbsp;</div>

const Game = ({focusKeyboard}) => (
    <div onClick={focusKeyboard}>
        <KeyboardContainer />

        <MessageBarContainer />
        <Spacer />

        <ScreenContainer />
        <InventoryContainer />
        <KeyBindingsContainer />
        <MessageLogContainer />
        <GameOverContainer />

        <PlayerInfoContainer />
        <Spacer />
        <ActionBarContainer />
        <Spacer />
        <DebugBarContainer />
    </div>
)

export default Game
