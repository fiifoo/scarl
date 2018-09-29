import { List, Record } from 'immutable'

const OFF_HAND_DIVISOR = 2

const Slot = Record({
    key: undefined,
    label: undefined,
})

const Group = Record({
    prop: undefined,
    label: undefined,
    slots: undefined,
    fillAll: undefined,
    getSlotStats: undefined,
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
        fillAll: () => true,
        getSlotStats: item => item[groups.Armor.prop].stats,
    }),
    Launcher: Group({
        prop: 'launcher',
        label: 'Launcher',
        slots:() => List([LauncherSlot]),
        fillAll: () => true,
        getSlotStats: item => item[groups.Launcher.prop].stats,
    }),
    Shield: Group({
        prop: 'shield',
        label: 'Shield',
        slots: () => List([OffHand]),
        fillAll: () => true,
        getSlotStats: item => item[groups.Shield.prop].stats,
    }),
    RangedWeapon: Group({
        prop: 'rangedWeapon',
        label: 'Ranged weapon',
        slots:() => List([RangedSlot]),
        fillAll: () => true,
        getSlotStats: item => item[groups.RangedWeapon.prop].stats,
    }),
    Weapon: Group({
        prop: 'weapon',
        label: 'Melee weapon',
        slots: () => List([MainHand, OffHand]),
        fillAll: item => !!item.weapon.twoHanded,
        getSlotStats: (item, slot) => {
            const stats = item[groups.Weapon.prop].stats

            return ! item.weapon.twoHanded && slot === OffHand ? getOffHandWeaponStats(stats) : stats
        }
    }),
}

const getOffHandWeaponStats = stats => {
    const melee = stats.melee
    const consumption = melee.consumption

    return {
        ...stats,
        melee: {
            attack: Math.floor(melee.attack / OFF_HAND_DIVISOR),
            damage: Math.floor(melee.damage / OFF_HAND_DIVISOR),
            consumption: {
                energy: consumption.energy / OFF_HAND_DIVISOR,
                materials: consumption.materials / OFF_HAND_DIVISOR,
            },
        },
    }
}
