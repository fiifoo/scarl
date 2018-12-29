import { fromJS, Map, Record } from 'immutable'

const readMap = raw => Map(raw).map(item => fromJS(item))
const writeMap = data => data.map(item => item.toJS()).map((v, k) => [k, v]).toArray()

const Catalogues = Record({
    contentSources: Map(),
    creatures: Map(),
    items: Map(),
    templates: Map(),
    templateSources: Map(),
    terrains: Map(),
    walls: Map(),
    widgets: Map(),
})
Catalogues.read = raw => Catalogues({
    contentSources: readMap(raw.contentSources),
    creatures: readMap(raw.creatures),
    items: readMap(raw.items),
    templates: readMap(raw.templates),
    templateSources: readMap(raw.templateSources),
    terrains: readMap(raw.terrains),
    walls: readMap(raw.walls),
    widgets: readMap(raw.widgets),
})
Catalogues.write = data => ({
    contentSources: writeMap(data.contentSources),
    creatures: writeMap(data.creatures),
    items: writeMap(data.items),
    templates: writeMap(data.templates),
    templateSources: writeMap(data.templateSources),
    terrains: writeMap(data.terrains),
    walls: writeMap(data.walls),
    widgets: writeMap(data.widgets),
})

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
    catalogues: Catalogues(),
    communications: Map(),
    factions: Map(),
    goals: Map(),
    keys: Map(),
    kinds: Kinds(),
    progressions: Map(),
    recipes: Map(),
    regions: Map(),
    sites: Map(),
    templates: Map(),
    themes: Map(),
    transports: Map(),
    worlds: Map(),
})
Data.read = raw => Data({
    areas: readMap(raw.areas),
    catalogues: Catalogues.read(raw.catalogues),
    communications: readMap(raw.communications),
    factions: readMap(raw.factions),
    goals: readMap(raw.goals),
    keys: readMap(raw.keys),
    kinds: Kinds.read(raw.kinds),
    progressions: readMap(raw.progressions),
    recipes: readMap(raw.recipes),
    regions: readMap(raw.regions),
    sites: readMap(raw.sites),
    templates: readMap(raw.templates),
    themes: readMap(raw.themes),
    transports: readMap(raw.transports),
    worlds: readMap(raw.worlds),
})
Data.write = data => ({
    areas: writeMap(data.areas),
    catalogues: Catalogues.write(data.catalogues),
    communications: writeMap(data.communications),
    factions: writeMap(data.factions),
    goals: writeMap(data.goals),
    keys: writeMap(data.keys),
    kinds: Kinds.write(data.kinds),
    progressions: writeMap(data.progressions),
    recipes: writeMap(data.recipes),
    regions: writeMap(data.regions),
    sites: writeMap(data.sites),
    templates: writeMap(data.templates),
    themes: writeMap(data.themes),
    transports: writeMap(data.transports),
    worlds: writeMap(data.worlds),
})

export default Data
