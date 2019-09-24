import { connect } from 'react-redux'
import { changePage, save, simulate } from '../actions/actions'
import App from './App.jsx'

const AppContainer = connect(
    state => ({
        fetchingSummary: state.ui.main.fetchingSummary,
        page: state.ui.main.page,
        readonly: state.readonly,
        saving: state.ui.main.saving,
        simulating: state.ui.main.simulating,
        unsaved: state.hashCode !== state.data.hashCode()
    }), {
        changePage,
        save,
        simulate,
    }
)(App)

export default AppContainer
