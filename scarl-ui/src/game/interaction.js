import { fromJS, List, Map, Record, Set } from 'immutable'
import * as utils from './utils'

export const Interaction = Record({
    location: undefined,
    action: undefined,
    data: undefined,
    kind: undefined,
    weight: undefined,
})

const Communicate = 'Communicate'
const EnterConduit = 'EnterConduit'
const HackItem = 'HackItem'
const PickItem = 'PickItem'
const UseCreature = 'UseCreature'
const UseDoor = 'UseDoor'
const UseItem = 'UseItem'

export const interactions = {
    Communicate,
    EnterConduit,
    HackItem,
    PickItem,
    UseCreature,
    UseDoor,
    UseItem,
}

const weights = Map({
    [Communicate]: [
        null,
        50,
    ],
    [EnterConduit]: [
        100,
        null,
    ],
    [HackItem]: [
        66,
        65
    ],
    [PickItem]: [
        80,
        null,
    ],
    [UseCreature]: [
        null,
        70,
    ],
    [UseDoor]: [
        null,
        60,
    ],
    [UseItem]: [
        91,
        90
    ],
})

const extractors = Map({
    [Communicate]: (location, fov) => (
        List(utils.getLocationCreatures(location, fov)).map(creature => ({
            data: {target: creature.id},
            kind: creature.kind,
        }))
    ),
    [EnterConduit]: (location, fov) => {
        const conduit = utils.getLocationConduit(location, fov)

        return conduit ? List([{
            data: {conduit},
            kind: null,
        }]) : List()
    },
    [HackItem]: (location, fov, keys) => {
        const items = List(utils.getLocationLockedItems(location, fov, keys))

        return items.map(item => ({
            data: {target: item.id},
            kind: item.kind,
        }))
    },
    [PickItem]: (location, fov) => (
        List(utils.getLocationPickableItems(location, fov)).map(item => ({
            data: {item: item.id},
            kind: item.kind,
        }))
    ),
    [UseCreature]: (location, fov) => (
        List(utils.getLocationUsableCreatures(location, fov)).map(creature => ({
            data: {target: creature.id},
            kind: creature.kind,
        }))
    ),
    [UseDoor]: (location, fov, keys) => {
        const door = utils.getLocationDoor(location, fov)

        return door && ! utils.isLockedItem(keys)(door) ? List([{
            data: {target: door.id},
            kind: door.kind,
        }]) : List()
    },
    [UseItem]: (location, fov, keys) => {
        const items = List(utils.getLocationUsableItems(location, fov))

        const isLocked = utils.isLockedItem(keys)
        const isNotLocked = item => ! isLocked(item)

        return items.filter(isNotLocked).map(item => ({
            data: {target: item.id},
            kind: item.kind,
        }))
    },
})

const descriptions = Map({
    [Communicate]: (kinds, interaction) => `Talk to ${kinds.creatures.get(interaction.kind).name}`,
    [EnterConduit]: () => 'Use stairs',
    [HackItem]: (kinds, interaction) => `Hack ${kinds.items.get(interaction.kind).name}`,
    [PickItem]: (kinds, interaction) => `Pick up ${kinds.items.get(interaction.kind).name}`,
    [UseCreature]: (kinds, interaction) => `Use ${kinds.creatures.get(interaction.kind).name}`,
    [UseDoor]: () => 'Use door',
    [UseItem]: (kinds, interaction) => `Use ${kinds.items.get(interaction.kind).name}`,
})

const seek = (weights, fov, keys, distance) => location => (
    weights.map((weights, action) => {
        const weight = weights[distance]
        const extractor = extractors.get(action)

        return weight ? extractor(location, fov, keys).map(({data, kind}) => Interaction({
            location,
            action,
            data,
            kind,
            weight,
        })) : List()
    }).toList().flatten(true)
)

export const seekInteractions = (player, fov, actions = undefined) => {
    if (actions && typeof actions === 'string') {
        actions = List([actions])
    }

    const location = player.creature.location
    const filteredWeights = actions ? weights.filter((_, action) => actions.includes(action)) : weights
    const keys = Set(player.keys.map(key => fromJS(key)))

    const here = seek(filteredWeights, fov, keys, 0)(location)

    const adjacent = List(utils.getAdjacentLocations(location)).flatMap(seek(filteredWeights, fov, keys, 1))

    return here.concat(adjacent).sort((a, b) => a.weight < b.weight ? 1 : -1)
}

export const getInteractionDescription = (kinds, interaction) => (
    descriptions.get(interaction.action)(kinds, interaction)
)
