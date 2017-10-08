import { Map, Record } from 'immutable'

const Models = Record({
    main: Map(),
    sub: Map(),
})
Models.read = raw => Models({
    main: Map(raw.main),
    sub: Map(raw.sub),
})
Models.choices = (models, data, modelId) => {
    const path = models.main.get(modelId).dataPath

    return data.getIn(path).map((_, id) => ({
        value: id,
        label: id
    })).toArray()
}

export default Models
