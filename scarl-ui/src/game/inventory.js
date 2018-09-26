import { List, Record } from 'immutable'
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
    prop: undefined,
})

export const tabs = List([
    Tab({
        key: 'Weapon',
        label: 'Melee weapons',
        prop: 'weapon',
    }),
    Tab({
        key: 'RangedWeapon',
        label: 'Ranged weapons',
        prop: 'rangedWeapon',
    }),
    Tab({
        key: 'Launcher',
        label: 'Launchers',
        prop: 'launcher',
    }),
    Tab({
        key: 'Shield',
        label: 'Shields',
        prop: 'shield',
    }),
    Tab({
        key: 'Armor',
        label: 'Wearables',
        prop: 'armor',
    }),
    Tab({
        key: 'Usable',
        label: 'Usables',
        prop: 'usable',
    }),
    Tab({
        key: 'Other',
        label: 'Other',
    }),
])

const props = tabs.map(x => x.prop).filter(x => x)

export const getItemActions = (actions, equipments, tab) => item => {
    const dropAction = Action({
        label: 'Drop',
        execute: () => actions.dropItem(item.id),
    })

    const recycleAction = item.recyclable ? (
        Action({
            label: 'Recycle',
            execute: () => actions.recycleItem(item.id),
            subs: List([
                Action({
                    label: 'Recycle',
                    execute: () => actions.recycleItem(item.id),
                }),
                dropAction,
            ]),
        })
    ) : null

    const recycleOrDrop = recycleAction || dropAction

    switch (tab.key) {
        case 'Other': {
            return List([
                recycleOrDrop,
            ])
        }
        case 'Usable': {
            return List([
                Action({
                    label: 'Use',
                    execute: () => actions.useItem(item.id),
                }),
                recycleOrDrop,
            ])
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
                            label: `Equip (${equipmentSlots[slot].label})`,
                            execute: () => actions.equipItem(item.id, slot),
                        }))
                    ) : List(),
                })
            )

            return List([
                equipAction,
                recycleOrDrop,
            ])
        }
    }
}

export const getItemActionsFlat = (actions, equipments, tab) => {
    const get = getItemActions(actions, equipments, tab)

    return item => get(item).flatMap(action => (
        action.subs.isEmpty() ? List([action]) : action.subs
    ))
}

export const getTabItems = (inventory, kinds) => tab => {
    const prop = tab.prop

    const items = prop ? (
        inventory.filter(createFilter(prop))
    ) : (
        inventory.filter(item => props.find(prop => item[prop] !== undefined) === undefined)
    )

    return items.sort(createSorter(kinds))
}

const createFilter = prop => item => item[prop] !== undefined
const createSorter = kinds => (a, b) => kinds.get(a.kind).name <= kinds.get(b.kind).name ? -1 : 1
