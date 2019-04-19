import { combineReducers } from 'redux'
import data from './data'
import hashCode from './hashCode'
import models from './models'
import summary from './summary'
import ui from './ui'
import tags from './tags'

export default combineReducers({
    data,
    hashCode,
    models,
    readonly: (state = false) => state,
    summary,
    ui,
    tags,
})
