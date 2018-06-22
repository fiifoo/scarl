import { List, Map, Record } from 'immutable'
import { groups as equipmentGroups, slots as equipmentSlots } from './equipment'

const Action = Record({
    label: undefined,
    execute: undefined,
    active: false,
    subs: List(),
})

const Tab = Record({
    key: undefined,
    label: undefined,
})

export const tabs = List([
    Tab({
        key: 'Weapon',
        label: 'Melee weapons',
    }),
    Tab({
        key: 'RangedWeapon',
        label: 'Ranged weapons',
    }),
    Tab({
        key: 'Launcher',
        label: 'Launchers',
    }),
    Tab({
        key: 'Shield',
        label: 'Shields',
    }),
    Tab({
        key: 'Armor',
        label: 'Wearables',
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
    Launcher: 'launcher',
    Shield: 'shield',
    Armor: 'armor',
    Usable: 'usable',
})

export const getItemActions = (actions, equipments, tab) => item => {
    const dropAction = Action({
        label: 'Drop',
        execute: () => actions.dropItem(item.id),
    })

    switch (tab.key) {
        case 'Other': {
            return [
                dropAction,
            ]
        }
        case 'Usable': {
            return [
                Action({
                    label: 'Use',
                    execute: () => actions.useItem(item.id),
                }),
                dropAction,
            ]
        }
        default: {
            const group = equipmentGroups[tab.key]
            const slots = group.slots(item)
            const fillAll = group.fillAll(item)

            const defaultSlot = slots.get(0)
            const equipped = equipments.contains(item.id)

            const equipAction = equipped ? (
                Action({
                    label: 'Unequip',
                    execute: () => actions.unequipItem(item.id),
                    active: true,
                })
            ) : (
                Action({
                    label: 'Equip',
                    execute: () => actions.equipItem(item.id, defaultSlot),
                    subs: !fillAll && slots.size > 1 ? (
                        slots.map(slot => Action({
                            label: equipmentSlots[slot].label,
                            execute: () => actions.equipItem(item.id, slot),
                        }))
                    ) : List(),
                })
            )

            return [
                equipAction,
                dropAction,
            ]
        }
    }
}

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
