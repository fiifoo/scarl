import { connect } from 'react-redux'
import { setEditorBrush, setEditorLocation } from '../actions/actions'
import Editor from '../components/area/Editor.jsx'
import { tabState } from '../reducers/ui/utils'

const EditorContainer = connect(
    state => ({
        editor: tabState(state.ui.editor),
    }), {
        setEditorBrush,
        setEditorLocation,
    }
)(Editor)

export default EditorContainer
