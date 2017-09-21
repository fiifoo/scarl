import { connect } from 'react-redux'
import { save } from '../actions/actions'
import App from '../components/App.jsx'

const AppContainer = connect(
    state => ({
        model: state.ui.model,
        readonly: state.readonly,
        saving: state.ui.saving,
    }), {
        save,
    }
)(App)

export default AppContainer
