import { connect } from 'react-redux'
import { setColumnEditorItems, setColumnEditorModel, setColumnEditorProperties, setItemValue } from '../actions/actions'
import ColumnEditor from '../components/columnEditor/ColumnEditor.jsx'

const ColumnEditorContainer = connect(
    state => ({
        data: state.data,
        models: state.models,
        ui: state.ui.columnEditor,
    }), {
        setModel: setColumnEditorModel,
        setItems: setColumnEditorItems,
        setProperties: setColumnEditorProperties,
        setItemValue,
    }
)(ColumnEditor)

export default ColumnEditorContainer
