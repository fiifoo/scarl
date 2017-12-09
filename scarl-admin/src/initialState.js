import Data from './data/Data'
import Models from './data/Models'
import { sanitize } from './data/utils'

const raw = window.STATE_FROM_SERVER

const data = Data.read(raw.data)
const models = Models.read(raw.models)

export default {
    data: sanitize(data, models),
    models,
    readonly: raw.readonly,
}
