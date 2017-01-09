import { connect } from 'react-redux'
import View from '../components/View.jsx'

const ViewContainer = connect(
    state => ({
        creatures: state.entities.creatures,
        items: state.entities.items,
    })
)(View)

export default ViewContainer
