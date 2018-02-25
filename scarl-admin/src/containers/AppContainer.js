import { connect } from 'react-redux'
import { changePage } from '../actions/actions'
import App from '../components/App.jsx'

const AppContainer = connect(
    state => ({
        page: state.ui.main.page,
    }), {
        changePage,
    }
)(App)

export default AppContainer
