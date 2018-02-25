import { connect } from 'react-redux'
import { selectModel } from '../actions/actions'
import ModelSelect from '../components/ModelSelect.jsx'
import { tabState } from '../reducers/ui/utils'

const ModelSelectContainer = connect(
    state => ({
        model: tabState(state.ui.form).model,
        models: state.models,
    }), {
        selectModel,
    }
)(ModelSelect)

export default ModelSelectContainer
