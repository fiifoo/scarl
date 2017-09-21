import Data from './data/Data'
import Models from './data/Models'

const raw = window.STATE_FROM_SERVER

export default {
    data: Data.read(raw.data),
    models: Models.read(raw.models),
    readonly: raw.readonly,
}
