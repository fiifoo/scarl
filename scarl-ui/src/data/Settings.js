import { Map, Record } from 'immutable'

const Settings = Record({
    equipmentSet: 0,
    equipmentSets: Map(),
    quickItems: Map(),
})

Settings.read = raw => Settings({
    equipmentSet: raw.equipmentSet,
    equipmentSets: Map(raw.equipmentSets).map(x => Map(x)),
    quickItems: Map(raw.quickItems),
})

export default Settings
