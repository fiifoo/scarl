import React from 'react'
import KeyboardContainer from '../containers/KeyboardContainer'
import MessageBarContainer from '../containers/MessageBarContainer'
import MessageLogContainer from '../containers/MessageLogContainer'
import PlayerInfoContainer from '../containers/PlayerInfoContainer'
import ScreenContainer from '../containers/ScreenContainer'
import StatisticsContainer from '../containers/StatisticsContainer'

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
            <KeyboardContainer />
            <MessageBarContainer />
            <ScreenContainer />
            <PlayerInfoContainer />
            <MessageLogContainer />
        </div>
    )
)

export default Game
