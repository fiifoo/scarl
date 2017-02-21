import { connect } from 'react-redux'
import { focusKeyboard } from '../actions/keyboardActions'
import View from '../components/View.jsx'

const ViewContainer = connect(
    state => ({
        connection: state.connection,
        fov: state.fov,
        kinds: state.kinds,
    }),
    {focusKeyboard}
)(View)

export default ViewContainer
