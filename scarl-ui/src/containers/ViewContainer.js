import { connect } from 'react-redux'
import { focusKeyboard } from '../actions/keyboardActions'
import View from '../components/View.jsx'

const ViewContainer = connect(
    state => ({
        fov: state.fov,
    }),
    {focusKeyboard}
)(View)

export default ViewContainer
