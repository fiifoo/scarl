import React from 'react'
import KeyboardContainer from '../containers/KeyboardContainer'
import MessageBarContainer from '../containers/MessageBarContainer'
import MessageLogContainer from '../containers/MessageLogContainer'
import PlayerInfoContainer from '../containers/PlayerInfoContainer'
import StatisticsContainer from '../containers/StatisticsContainer'
import ViewContainer from '../containers/ViewContainer'

const Game = ({connection, gameOver}) => ! connection ? <div></div> : (
    gameOver ? (
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
            <ViewContainer />
            <PlayerInfoContainer />
            <MessageLogContainer />
        </div>
    )
)

export default Game
