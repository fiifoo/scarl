import React from 'react'
import { connect } from 'react-redux'
import { createExistingGame, createNewGame, setNewGamePlayer } from '../../actions/createActions'
import CreateGame from './CreateGame.jsx'

const ComponentIf = ({connection, created, ...props}) => (
    connection && ! created ? (
        <CreateGame {...props} />
    ) : (
        <div />
    )
)

const GameContainer = connect(
    state => ({
        connection: state.connection,
        created: state.game.created,
        error: state.ui.create.error,
        games: state.games,
        player: state.ui.create.player,
    }), {
        createExistingGame,
        createNewGame,
        setNewGamePlayer,
    }
)(ComponentIf)

export default GameContainer
