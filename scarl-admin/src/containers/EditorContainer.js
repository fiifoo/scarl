import { connect } from 'react-redux'
import { setEditorBrush, setEditorLocation } from '../actions/actions'
import Editor from '../components/area/Editor.jsx'

const EditorContainer = connect(
    state => ({
        editor: state.ui.editor,
    }), {
        setEditorBrush,
        setEditorLocation,
    }
)(Editor)

export default EditorContainer
