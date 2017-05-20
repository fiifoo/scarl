import React from 'react'
import ActionBarContainer from '../containers/ActionBarContainer'
import InventoryContainer from '../containers/InventoryContainer'
import KeyBindingsContainer from '../containers/KeyBindingsContainer'
import KeyboardContainer from '../containers/KeyboardContainer'
import MessageBarContainer from '../containers/MessageBarContainer'
import MessageLogContainer from '../containers/MessageLogContainer'
import PlayerInfoContainer from '../containers/PlayerInfoContainer'
import ScreenContainer from '../containers/ScreenContainer'
import StatisticsContainer from '../containers/StatisticsContainer'

const Spacer = () => <div>&nbsp;</div>

const Game = ({game, focusKeyboard}) => (
    game.running ? (
        <div onClick={focusKeyboard}>
            <KeyboardContainer />

            <MessageBarContainer />
            <Spacer />

            <div className="game-main-view">
                <ScreenContainer />

                <InventoryContainer />
                <KeyBindingsContainer />
                <MessageLogContainer />
            </div>

            <PlayerInfoContainer />
            <Spacer />
            <ActionBarContainer />
        </div>
    ) : game.over ? (
        <div>
            <h3>Your journey is over</h3>
            <StatisticsContainer />
            <h4>Latest messages:</h4>
            <MessageLogContainer />
        </div>
    ) : (
        <div></div>
    )
)

export default Game
