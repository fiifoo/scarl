import { connect } from 'react-redux'
import { selectModel } from '../actions/actions'
import ModelSelect from '../components/ModelSelect.jsx'

const ModelSelectContainer = connect(
    state => ({
        model: state.ui.main.model,
        models: state.models,
    }), {
        selectModel,
    }
)(ModelSelect)

export default ModelSelectContainer
