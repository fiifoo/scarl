import { List, Map, Record } from 'immutable'
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
const HackCreature = 'HackCreature'
const HackItem = 'HackItem'
const PickItem = 'PickItem'
const RecycleItem = 'RecycleItem'
const UseCreature = 'UseCreature'
const UseDoor = 'UseDoor'
const UseItem = 'UseItem'

export const interactions = {
    Communicate,
    EnterConduit,
    HackCreature,
    HackItem,
    PickItem,
    RecycleItem,
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
    [HackCreature]: [
        66,
        65
    ],
    [HackItem]: [
        66,
        65
    ],
    [PickItem]: [
        80,
        null,
    ],
    [RecycleItem]: [
        79,
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
    [HackCreature]: (location, fov, keys) => (
        List(utils.getLocationLockedCreatures(location, fov, keys)).map(creature => ({
            data: {target: creature.id},
            kind: creature.kind,
        }))
    ),
    [HackItem]: (location, fov, keys) => (
        List(utils.getLocationLockedItems(location, fov, keys)).map(item => ({
            data: {target: item.id},
            kind: item.kind,
        }))
    ),
    [PickItem]: (location, fov) => (
        List(utils.getLocationPickableItems(location, fov)).map(item => ({
            data: {item: item.id},
            kind: item.kind,
        }))
    ),
    [RecycleItem]: (location, fov) => (
        List(utils.getLocationRecyclableItems(location, fov)).map(item => ({
            data: {target: item.id},
            kind: item.kind,
        }))
    ),
    [UseCreature]: (location, fov, keys) => (
        List(utils.getLocationUsableCreatures(location, fov, keys)).map(creature => ({
            data: {target: creature.id},
            kind: creature.kind,
        }))
    ),
    [UseDoor]: (location, fov, keys) => {
        const door = utils.getLocationDoor(location, fov)

        return door && ! utils.isLocked(keys)(door) ? List([{
            data: {target: door.id},
            kind: door.kind,
        }]) : List()
    },
    [UseItem]: (location, fov, keys) => (
        List(utils.getLocationUsableItems(location, fov, keys)).map(item => ({
            data: {target: item.id},
            kind: item.kind,
        }))
    ),
})

const descriptions = Map({
    [Communicate]: (kinds, interaction) => `Talk to ${kinds.creatures.get(interaction.kind).name}`,
    [EnterConduit]: () => 'Use stairs',
    [HackCreature]: (kinds, interaction) => `Hack ${kinds.creatures.get(interaction.kind).name}`,
    [HackItem]: (kinds, interaction) => `Hack ${kinds.items.get(interaction.kind).name}`,
    [PickItem]: (kinds, interaction) => `Pick up ${kinds.items.get(interaction.kind).name}`,
    [RecycleItem]: (kinds, interaction) => `Recycle ${kinds.items.get(interaction.kind).name}`,
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
    const keys = player.keys

    const here = seek(filteredWeights, fov, keys, 0)(location)

    const adjacent = List(utils.getAdjacentLocations(location)).flatMap(seek(filteredWeights, fov, keys, 1))

    return here.concat(adjacent).sort((a, b) => a.weight < b.weight ? 1 : -1)
}

export const getInteractionDescription = (kinds, interaction) => (
    descriptions.get(interaction.action)(kinds, interaction)
)
