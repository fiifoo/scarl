import { connect } from 'react-redux'
import { move } from '../actions/actions'
import View from '../components/View.jsx'

const ViewContainer = connect(
    state => ({
        creatures: state.entities.creatures,
        fov: state.fov,
        items: state.entities.items,
    }),
    {move}
)(View)

export default ViewContainer
