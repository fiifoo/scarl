import React from 'react'
import GameViewsContainer from '../game/GameViewsContainer'
import InteractionSelectContainer from '../game/InteractionSelectContainer'
import KeyboardContainer from '../game/KeyboardContainer'
import LookDescriptionContainer from '../game/LookDescriptionContainer'
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
                <InteractionSelectContainer />
                <LookDescriptionContainer />
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
