import { connect } from 'react-redux'
import { closeConnection, openConnection, ping } from '../actions/connectionActions'
import Connection from '../components/Connection.jsx'

const ConnectionContainer = connect(
    state => ({
        closingConnection: state.ui.closingConnection,
        connection: state.connection,
        gameOver: state.gameOver,
        openingConnection: state.ui.openingConnection
    }), {
        closeConnection,
        openConnection,
        ping,
    }
)(Connection)

export default ConnectionContainer
