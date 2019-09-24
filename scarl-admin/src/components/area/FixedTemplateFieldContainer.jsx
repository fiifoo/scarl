import { connect } from 'react-redux'
import { setAreaEditorVisible } from '../../actions/actions'
import { tabState } from '../../reducers/ui/utils'
import FixedTemplateField from './FixedTemplateField.jsx'

const FixedTemplateFieldContainer = connect(
    state => ({
        areaEditorVisible: tabState(state.ui.areaEditor).visible,
    }), {
        setAreaEditorVisible
    }
)(FixedTemplateField)

export default FixedTemplateFieldContainer
