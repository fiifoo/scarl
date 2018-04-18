import React from 'react'
import Header from '../Header.jsx'
import { formatDateTime } from '../../utils/date'

const NewGame = ({player, createNewGame, setNewGamePlayer}) => (
    <div>
        <h4>New Game</h4>
        <form className="form-inline" onSubmit={createNewGame}>
            <div className="form-group">
                <label htmlFor="create-game-player">Player name:</label>
                <input
                    type="text"
                    id="create-game-player"
                    className="form-control"
                    value={player || ''}
                    onChange={event => setNewGamePlayer(event.target.value)}
                    autoFocus />
                <button
                    type="submit"
                    className="btn btn-default">
                    Create
                </button>
            </div>
        </form>
    </div>
)

const ExistingGames = ({games, createExistingGame}) => {

    const renderGame = game => (
        <tr key={game.id}>
            <td>{game.player}</td>
            <td>{formatDateTime(game.createdAt)}</td>
            <td>{formatDateTime(game.lastPlayedAt)}</td>
            <td>
                <button
                    type="button"
                    className="btn btn-default"
                    disabled={game.running}
                    onClick={() => createExistingGame(game.id)}>
                    Load
                </button>
            </td>
            <td className="text-danger">{game.running && 'Running'}</td>
        </tr>
    )

    return (
        <div style={{marginTop: '3em'}}>
            <h4>Existing Games</h4>
            <table className="table">
                <thead>
                    <tr>
                        <th style={{width: '100%'}}>Name</th>
                        <th>Created</th>
                        <th>Last played</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {games.map(renderGame)}
                </tbody>
            </table>
        </div>
    )
}

const ExistingGamesIf = props => (
    props.games.length > 0 ? (
        <ExistingGames {...props} />
    ) : (
        <div></div>
    )
)

const CreateGame = ({error, games, player, createExistingGame, createNewGame, setNewGamePlayer}) => (
    <div>
        <Header />
        <div className="container-fluid">
            <div className="row">
                <div className="col-md-6">
                    <div className="panel panel-default">
                        <div className="panel-body">
                            {error && <div className="alert alert-danger">Creating game failed.</div>}
                            <NewGame player={player} createNewGame={createNewGame} setNewGamePlayer={setNewGamePlayer} />
                            <ExistingGamesIf games={games} createExistingGame={createExistingGame} />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
)

export default CreateGame
