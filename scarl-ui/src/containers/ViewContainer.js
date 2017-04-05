import { connect } from 'react-redux'
import { focusKeyboard } from '../actions/keyboardActions'
import View from '../components/View.jsx'

const ViewContainer = connect(
    state => ({
        area: state.area,
        connection: state.connection,
        fov: state.fov,
        kinds: state.kinds,
        map: state.map,
    }),
    {focusKeyboard}
)(View)

export default ViewContainer
