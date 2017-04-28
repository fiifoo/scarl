import { List, Record } from 'immutable'

const Slot = Record({
    key: undefined,
    label: undefined,
})

const Group = Record({
    prop: undefined,
    label: undefined,
    getSlots: undefined,
})

export const slots = {
    MainHand: Slot({
        key: 'MainHand',
        label: 'Main hand',
    }),
    OffHand: Slot({
        key: 'OffHand',
        label: 'Off-hand',
    }),
    HeadArmor: Slot({
        key: 'HeadArmor',
        label: 'Head armor',
    }),
    ChestArmor: Slot({
        key: 'ChestArmor',
        label: 'Chest armor',
    }),
    HandArmor: Slot({
        key: 'HandArmor',
        label: 'Hand armor',
    }),
    LegArmor: Slot({
        key: 'LegArmor',
        label: 'Leg armor',
    }),
    FootArmor: Slot({
        key: 'FootArmor',
        label: 'Foot armor',
    }),
}

export const groups = {
    Armor: Group({
        prop: 'armor',
        label: 'Armors',
        getSlots: armor => List([armor.slot]),
    }),
    Shield: Group({
        prop: 'shield',
        label: 'Shields',
        getSlots: () => List([slots.OffHand.key]),
    }),
    Weapon: Group({
        prop: 'weapon',
        label: 'Weapons',
        getSlots: weapon => List(weapon.twoHanded
            ? [slots.MainHand.key]
            : [slots.MainHand.key, slots.OffHand.key]
        ),
    }),
}
