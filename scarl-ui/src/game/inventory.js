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

    const recycleAction = item.recyclable ? (
        Action({
            label: 'Recycle',
            execute: () => actions.recycleItem(item.id),
        })
    ) : null

    const exists = x => x != null

    switch (tab.key) {
        case 'Other': {
            return List([
                recycleAction,
                dropAction,
            ]).filter(exists)
        }
        case 'Usable': {
            return List([
                Action({
                    label: 'Use',
                    execute: () => actions.useItem(item.id),
                }),
                recycleAction,
                dropAction,
            ]).filter(exists)
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

            return List([
                equipAction,
                recycleAction,
                dropAction,
            ]).filter(exists)
        }
    }
}

export const getItemActionsFlat = (actions, equipments, tab) => {
    const get = getItemActions(actions, equipments, tab)

    return item => get(item).flatMap(action => (
        action.subs.isEmpty() ? List([action]) : action.subs.map(sub => (
            sub.set('label', `${action.label} (${sub.label})`)
        ))
    ))
}

export const getTabItems = (inventory, kinds) => tab => {
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
