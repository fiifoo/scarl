import React from 'react'
import { connect } from 'react-redux'
import { focusKeyboard } from '../../actions/keyboard'
import Game from './Game.jsx'

const ComponentIf = ({running, ...props}) => (
    running ? (
        <Game {...props} />
    ) : (
        <div />
    )
)

const GameContainer = connect(
    state => ({
        running: state.game.running,
    }), {
        focusKeyboard,
    }
)(ComponentIf)

export default GameContainer
