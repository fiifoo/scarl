import { List, Record } from 'immutable'

const Slot = Record({
    key: undefined,
    label: undefined,
})

const Group = Record({
    prop: undefined,
    label: undefined,
    slots: undefined,
    fillAll: undefined,
})

export const MainHand = 'Equipment.MainHand'
export const OffHand = 'Equipment.OffHand'
export const RangedSlot = 'Equipment.RangedSlot'
export const LauncherSlot = 'Equipment.LauncherSlot'
export const HeadArmor = 'Equipment.HeadArmor'
export const BodyArmor = 'Equipment.BodyArmor'
export const HandArmor = 'Equipment.HandArmor'
export const FootArmor = 'Equipment.FootArmor'

export const slots = {
    [MainHand]: Slot({
        key: MainHand,
        label: 'Main hand',
    }),
    [OffHand]: Slot({
        key: OffHand,
        label: 'Off-hand',
    }),
    [RangedSlot]: Slot({
        key: RangedSlot,
        label: 'Ranged weapon',
    }),
    [LauncherSlot]: Slot({
        key: LauncherSlot,
        label: 'Launcher',
    }),
    [HeadArmor]: Slot({
        key: HeadArmor,
        label: 'Head',
    }),
    [BodyArmor]: Slot({
        key: BodyArmor,
        label: 'Body',
    }),
    [HandArmor]: Slot({
        key: HandArmor,
        label: 'Hands',
    }),
    [FootArmor]: Slot({
        key: FootArmor,
        label: 'Feet',
    }),
}

export const groups = {
    Armor: Group({
        prop: 'armor',
        label: 'Wearable',
        slots: item => List([item.armor.slot]),
        fillAll: () => true
    }),
    Launcher: Group({
        prop: 'launcher',
        label: 'Launcher',
        slots:() => List([LauncherSlot]),
        fillAll: () => true
    }),
    Shield: Group({
        prop: 'shield',
        label: 'Shield',
        slots: () => List([OffHand]),
        fillAll: () => true
    }),
    RangedWeapon: Group({
        prop: 'rangedWeapon',
        label: 'Ranged weapon',
        slots:() => List([RangedSlot]),
        fillAll: () => true
    }),
    Weapon: Group({
        prop: 'weapon',
        label: 'Melee weapon',
        slots: () => List([MainHand, OffHand]),
        fillAll: item => !!item.weapon.twoHanded
    }),
}
