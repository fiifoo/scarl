import { fromJS, Map, Record } from 'immutable'

const readMap = raw => Map(raw).map(item => fromJS(item))
const writeMap = data => data.map(item => item.toJS()).map((v, k) => [k, v]).toArray()

const Kinds = Record({
    creatures: Map(),
    items: Map(),
    terrains: Map(),
    walls: Map(),
    widgets: Map(),
})
Kinds.read = raw => Kinds({
    creatures: readMap(raw.creatures),
    items: readMap(raw.items),
    terrains: readMap(raw.terrains),
    walls: readMap(raw.walls),
    widgets: readMap(raw.widgets),
})
Kinds.write = data => ({
    creatures: writeMap(data.creatures),
    items: writeMap(data.items),
    terrains: writeMap(data.terrains),
    walls: writeMap(data.walls),
    widgets: writeMap(data.widgets),
})

const Data = Record({
    areas: Map(),
    communications: Map(),
    factions: Map(),
    keys: Map(),
    kinds: Kinds(),
    progressions: Map(),
    templates: Map(),
    themes: Map(),
})
Data.read = raw => Data({
    areas: readMap(raw.areas),
    communications: readMap(raw.communications),
    factions: readMap(raw.factions),
    keys: readMap(raw.keys),
    kinds: Kinds.read(raw.kinds),
    progressions: readMap(raw.progressions),
    templates: readMap(raw.templates),
    themes: readMap(raw.themes),
})
Data.write = data => ({
    areas: writeMap(data.areas),
    communications: writeMap(data.communications),
    factions: writeMap(data.factions),
    keys: writeMap(data.keys),
    kinds: Kinds.write(data.kinds),
    progressions: writeMap(data.progressions),
    templates: writeMap(data.templates),
    themes: writeMap(data.themes),
})

export default Data
