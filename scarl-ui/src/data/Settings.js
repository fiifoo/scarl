import { Map, Record } from 'immutable'

const Settings = Record({
    quickItems: Map(),
})

Settings.read = raw => Settings({
    quickItems: Map(raw.quickItems),
})

export default Settings
