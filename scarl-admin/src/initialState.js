import Data from './data/Data'
import Models from './data/Models'
import { getTags, sanitize } from './data/utils'
import { readUi } from './data/ui/utils'

const raw = window.STATE_FROM_SERVER

const models = Models.read(raw.models)
const data = sanitize(Data.read(raw.data), models)
const tags = getTags(data, models)
const ui = raw.ui ? readUi(raw.ui) : undefined

export default {
    data,
    hashCode: data.hashCode(),
    initialUi: ui,
    models,
    readonly: raw.readonly,
    tags,
    ui,
}
