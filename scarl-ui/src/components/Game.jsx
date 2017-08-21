import React from 'react'
import ActionBarContainer from '../containers/ActionBarContainer'
import DebugBarContainer from '../containers/DebugBarContainer'
import InventoryContainer from '../containers/InventoryContainer'
import KeyBindingsContainer from '../containers/KeyBindingsContainer'
import KeyboardContainer from '../containers/KeyboardContainer'
import MessageBarContainer from '../containers/MessageBarContainer'
import MessageLogContainer from '../containers/MessageLogContainer'
import PlayerInfoContainer from '../containers/PlayerInfoContainer'
import ScreenContainer from '../containers/ScreenContainer'
import StatisticsContainer from '../containers/StatisticsContainer'

const Spacer = () => <div>&nbsp;</div>

const Game = ({focusKeyboard}) => (
    <div onClick={focusKeyboard}>
        <KeyboardContainer />

        <MessageBarContainer />
        <Spacer />

        <div className="game-main-view">
            <ScreenContainer />

            <InventoryContainer />
            <KeyBindingsContainer />
            <StatisticsContainer />
            <MessageLogContainer />
        </div>

        <PlayerInfoContainer />
        <Spacer />
        <ActionBarContainer />
        <Spacer />
        <DebugBarContainer />
    </div>
)

export default Game
