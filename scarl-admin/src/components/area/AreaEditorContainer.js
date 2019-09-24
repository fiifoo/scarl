import { connect } from 'react-redux'
import { setAreaEditorBrush, setAreaEditorLocation } from '../../actions/actions'
import { tabState } from '../../reducers/ui/utils'
import Editor from './Editor.jsx'

const AreaEditorContainer = connect(
    state => ({
        editor: tabState(state.ui.areaEditor),
    }), {
        setEditorBrush: setAreaEditorBrush,
        setEditorLocation: setAreaEditorLocation,
    }
)(Editor)

export default AreaEditorContainer
