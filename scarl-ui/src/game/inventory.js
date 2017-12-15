import { List, Map, Record } from 'immutable'

const Tab = Record({
    key: undefined,
    label: undefined,
})

export const tabs = List([
    Tab({
        key: 'Weapon',
        label: 'Weapons',
    }),
    Tab({
        key: 'RangedWeapon',
        label: 'Ranged weapons',
    }),
    Tab({
        key: 'Shield',
        label: 'Shields',
    }),
    Tab({
        key: 'Armor',
        label: 'Armors',
    }),
    Tab({
        key: 'Usable',
        label: 'Usables',
    }),
    Tab({
        key: 'Other',
        label: 'Other',
    }),
])

const props = Map({
    Weapon: 'weapon',
    RangedWeapon: 'rangedWeapon',
    Shield: 'shield',
    Armor: 'armor',
    Usable: 'usable',
})

export const createItemReader = (inventory, kinds) => tab => {
    const prop = props.get(tab.key)

    const items = prop ? (
        inventory.filter(createFilter(prop))
    ) : (
        inventory.filter(item => props.find(prop => item[prop] !== undefined) === undefined)
    )

    return items.sort(createSorter(kinds))
}

const createFilter = prop => item => item[prop] !== undefined
const createSorter = kinds => (a, b) => kinds.get(a.kind).name <= kinds.get(b.kind).name ? -1 : 1
