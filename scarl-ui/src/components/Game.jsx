import React from 'react'
import ActionBarContainer from '../containers/ActionBarContainer'
import KeyboardContainer from '../containers/KeyboardContainer'
import MessageBarContainer from '../containers/MessageBarContainer'
import MessageLogContainer from '../containers/MessageLogContainer'
import PlayerInfoContainer from '../containers/PlayerInfoContainer'
import ScreenContainer from '../containers/ScreenContainer'
import StatisticsContainer from '../containers/StatisticsContainer'

const Spacer = () => <div>&nbsp;</div>

const Game = ({game}) => ! game.started ? <div></div> : (
    game.over ? (
        <div>
            <h3>Your journey is over</h3>
            <StatisticsContainer />
            <h4>Latest messages:</h4>
            <MessageLogContainer />
        </div>
    ) : (
        <div>
            <MessageLogContainer />
            <KeyboardContainer />
            <MessageBarContainer />
            <ScreenContainer />
            <PlayerInfoContainer />
            <Spacer />
            <ActionBarContainer />
        </div>
    )
)

export default Game
