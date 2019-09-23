import { combineReducers } from 'redux'
import data from './data'
import hashCode from './hashCode'
import initialUi from './initialUi'
import models from './models'
import summary from './summary'
import ui from './ui'
import tags from './tags'

export default combineReducers({
    data,
    hashCode,
    initialUi,
    models,
    readonly: (state = false) => state,
    summary,
    ui,
    tags,
})
