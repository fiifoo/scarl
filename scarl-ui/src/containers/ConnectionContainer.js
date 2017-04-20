import { connect } from 'react-redux'
import { closeConnection, openConnection } from '../actions/connectionActions'
import Connection from '../components/Connection.jsx'

const ConnectionContainer = connect(
    state => ({
        closingConnection: state.ui.closingConnection,
        connection: state.connection,
        openingConnection: state.ui.openingConnection
    }), {
        closeConnection,
        openConnection,
    }
)(Connection)

export default ConnectionContainer
