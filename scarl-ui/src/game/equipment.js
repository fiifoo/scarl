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

const MainHand = 'Equipment.MainHand'
const OffHand = 'Equipment.OffHand'
const RangedSlot = 'Equipment.RangedSlot'
const LauncherSlot = 'Equipment.LauncherSlot'
const HeadArmor = 'Equipment.HeadArmor'
const BodyArmor = 'Equipment.BodyArmor'
const HandArmor = 'Equipment.HandArmor'
const FootArmor = 'Equipment.FootArmor'

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
        label: 'Wearables',
        getSlots: item => List([item.armor.slot]),
    }),
    Launcher: Group({
        prop: 'launcher',
        label: 'Launchers',
        getSlots:() => List([LauncherSlot]),
    }),
    Shield: Group({
        prop: 'shield',
        label: 'Shields',
        getSlots: () => List([OffHand]),
    }),
    RangedWeapon: Group({
        prop: 'rangedWeapon',
        label: 'Ranged weapons',
        getSlots:() => List([RangedSlot]),
    }),
    Weapon: Group({
        prop: 'weapon',
        label: 'Melee weapons',
        getSlots: item => List(item.weapon.twoHanded
            ? [MainHand]
            : [MainHand, OffHand]
        ),
    }),
}
