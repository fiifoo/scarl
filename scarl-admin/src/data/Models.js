import { Map, Record } from 'immutable'

const Models = Record({
    main: Map(),
    sub: Map(),
})
Models.read = raw => Models({
    main: Map(raw.main),
    sub: Map(raw.sub),
})

export default Models
