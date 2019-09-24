import { connect } from 'react-redux'
import { setColumnEditorItems, setColumnEditorModel, setColumnEditorProperties, setItemValue, addTag } from '../../actions/actions'
import ColumnEditor from './ColumnEditor.jsx'

const ColumnEditorContainer = connect(
    state => ({
        data: state.data,
        models: state.models,
        tags: state.tags,
        ui: state.ui.columnEditor,
    }), {
        setModel: setColumnEditorModel,
        setItems: setColumnEditorItems,
        setProperties: setColumnEditorProperties,
        setItemValue,
        addTag,
    }
)(ColumnEditor)

export default ColumnEditorContainer
