import { connect } from 'react-redux'
import { save } from '../actions/actions'
import App from '../components/App.jsx'

const AppContainer = connect(
    state => ({
        model: state.ui.main.model,
        readonly: state.readonly,
        saving: state.ui.main.saving,
    }), {
        save,
    }
)(App)

export default AppContainer
