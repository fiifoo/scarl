import { connect } from 'react-redux'
import { setAreaEditorBrush, setAreaEditorLocation } from '../actions/actions'
import Editor from '../components/area/Editor.jsx'
import { tabState } from '../reducers/ui/utils'

const AreaEditorContainer = connect(
    state => ({
        editor: tabState(state.ui.areaEditor),
    }), {
        setEditorBrush: setAreaEditorBrush,
        setEditorLocation: setAreaEditorLocation,
    }
)(Editor)

export default AreaEditorContainer
