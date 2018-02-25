import { combineReducers } from 'redux'
import form from './form'
import editor from './editor'
import main from './main'
import summary from './summary'

export default combineReducers({
    form,
    editor,
    main,
    summary,
})
