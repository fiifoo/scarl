import { List, Map, Record } from 'immutable'
import * as types from '../actions/actionTypes'
import SolarSystem from '../system/SolarSystem'

const hasControlledTransport = world => world.transports.find(x => x.hub === world.site) !== undefined
const canEmbark = world => world.transportRegions.find((regionId, transportId) => {
    const region = world.regions.get(regionId)
    const category = world.transports.get(transportId).category

    return region.exits.get(category, List()).contains(world.site)
}) !== undefined

const hasActions = world => hasControlledTransport(world) || canEmbark(world)

const WorldInfo = Record({
    site: null,
    regions: Map(),
    siteRegions: Map(),
    transportRegions: Map(),
    transports: Map(),
    system: null,
    hasActions: false, // cache
})
WorldInfo.read = ({regions, siteRegions, transportRegions, transports, system, ...data}) => {
    const world = WorldInfo({
        ...data,
        regions: Map(regions).map(Region.read),
        siteRegions: Map(siteRegions),
        transportRegions: Map(transportRegions),
        transports: Map(transports),
        system: SolarSystem.read(system),
    })

    return world.set('hasActions', hasActions(world))
}

const Region = Record({
    id: undefined,
    stellarBody: null,
    entrances: Map(),
    exits: Map(),
})
Region.read = ({entrances, exits, ...data}) => Region({
    ...data,
    entrances: Map(entrances).map(x => List(x)),
    exits: Map(exits).map(x => List(x)),
})

export { WorldInfo }

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
