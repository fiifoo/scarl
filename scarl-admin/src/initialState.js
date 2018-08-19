import Data from './data/Data'
import Models from './data/Models'
import { getTags, sanitize } from './data/utils'

const raw = window.STATE_FROM_SERVER

const models = Models.read(raw.models)
const data = sanitize(Data.read(raw.data), models)
const tags = getTags(data, models)

export default {
    data,
    models,
    readonly: raw.readonly,
    tags,
}
