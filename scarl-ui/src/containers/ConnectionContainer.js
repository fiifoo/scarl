import { connect } from 'react-redux'
import { closeConnection, openConnection, ping } from '../actions/actions'
import Connection from '../components/Connection.jsx'

const ConnectionContainer = connect(
    state => ({
        closingConnection: state.ui.closingConnection,
        connection: state.connection,
        openingConnection: state.ui.openingConnection
    }), {
        closeConnection,
        openConnection,
        ping,
    }
)(Connection)

export default ConnectionContainer
