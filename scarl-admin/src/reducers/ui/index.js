import { combineReducers } from 'redux'
import editor from './editor'
import main from './main'
import summary from './summary'

export default combineReducers({
    editor,
    main,
    summary,
})
