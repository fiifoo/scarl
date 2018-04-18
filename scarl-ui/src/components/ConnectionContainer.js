import { connect } from 'react-redux'
import { closeConnection, openConnection } from '../actions/connectionActions'
import Connection from './Connection.jsx'

const ConnectionContainer = connect(
    state => ({
        closingConnection: state.ui.main.closingConnection,
        connection: state.connection,
        openingConnection: state.ui.main.openingConnection
    }), {
        closeConnection,
        openConnection,
    }
)(Connection)

export default ConnectionContainer
