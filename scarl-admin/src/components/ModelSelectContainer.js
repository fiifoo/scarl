import { connect } from 'react-redux'
import { selectModel } from '../actions/actions'
import { tabState } from '../reducers/ui/utils'
import ModelSelect from './ModelSelect.jsx'

const ModelSelectContainer = connect(
    state => ({
        model: tabState(state.ui.form).model,
        models: state.models,
    }), {
        selectModel,
    }
)(ModelSelect)

export default ModelSelectContainer
