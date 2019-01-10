import { List, Map, Record } from 'immutable'
import * as types from '../actions/actionTypes'

const WorldInfo = Record({
    site: null,
    regions: Map(),
    siteRegions: Map(),
    transportRegions: Map(),
    transports: Map(),
})
WorldInfo.read = data => WorldInfo({
    site: data.site,
    regions: Map(data.regions).map(Region.read),
    siteRegions: Map(data.siteRegions),
    transportRegions: Map(data.transportRegions),
    transports: Map(data.transports),
})

const Region = Record({
    id: undefined,
    entrances: Map(),
    exits: Map(),
})
Region.read = data => Region({
    id: data.id,
    entrances: Map(data.entrances).map(x => List(x)),
    exits: Map(data.exits).map(x => List(x)),
})

export default (state = WorldInfo(), action) => {
    switch (action.type) {
        case types.CONNECTION_CLOSED: {
            return WorldInfo()
        }
        case types.RECEIVE_GAME_START:
        case types.RECEIVE_AREA_CHANGE:
        case types.RECEIVE_WORLD_INFO: {
            return WorldInfo.read(action.data)
        }
        default: {
            return state
        }
    }
}
