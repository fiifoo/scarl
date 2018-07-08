import { combineReducers } from 'redux'
import form from './form'
import areaEditor from './areaEditor'
import columnEditor from './columnEditor'
import main from './main'
import summary from './summary'

export default combineReducers({
    form,
    areaEditor,
    columnEditor,
    main,
    summary,
})
