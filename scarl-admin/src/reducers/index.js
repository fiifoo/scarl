import { combineReducers } from 'redux'
import data from './data'
import models from './models'
import summary from './summary'
import ui from './ui'
import tags from './tags'

export default combineReducers({
    data,
    models,
    readonly: (state = false) => state,
    summary,
    ui,
    tags,
})
