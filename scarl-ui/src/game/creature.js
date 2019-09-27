import { fromJS, List, Map, OrderedMap, Set } from 'immutable'

export const addStats = (a, b) => {
    a = fromJS(a)
    b = fromJS(b)

    return stats.reduce((a, _, stat) => (
        a.setIn(stat, addStat(stat, a, b))
    ), a).toJS()

}
const addStat = (stat, a, b) => {
    if (optionStats.contains(stat)) {
        return a.getIn(stat, b.getIn(stat))
    } else {
        return a.getIn(stat) + b.getIn(stat)
    }
}

export const stats = OrderedMap([
    [['speed'], 'Speed'],
    [['sight', 'range'], 'Sight range'],
    [['sight', 'sensors'], 'Sensors'],
    [['skill', 'hacking'], 'Hacking'],
    [['defence'], 'Defence'],
    [['armor'], 'Armor'],
    [['resistance'], 'Resistance'],
    [['health', 'max'], 'Health'],
    [['health', 'regen'], 'Health regeneration'],
    [['energy', 'max'], 'Energy'],
    [['energy', 'regen'], 'Energy regeneration'],
    [['materials', 'max'], 'Materials'],
    [['materials', 'regen'], 'Materials regeneration'],
    [['melee', 'attack'], 'Melee attack'],
    [['melee', 'damage'], 'Melee damage'],
    [['melee', 'stance'], 'Melee stance'],
    [['melee', 'consumption', 'energy'], 'Melee energy consumption'],
    [['melee', 'consumption', 'materials'], 'Melee materials consumption'],
    [['ranged', 'attack'], 'Ranged attack'],
    [['ranged', 'damage'], 'Ranged damage'],
    [['ranged', 'range'], 'Ranged attack range'],
    [['ranged', 'stance'], 'Ranged stance'],
    [['ranged', 'consumption', 'energy'], 'Ranged energy consumption'],
    [['ranged', 'consumption', 'materials'], 'Ranged materials consumption'],
    [['launcher', 'range'], 'Launcher attack range'],
    [['launcher', 'stance'], 'Launcher stance'],
    [['launcher', 'consumption', 'energy'], 'Launcher energy consumption'],
    [['launcher', 'consumption', 'materials'], 'Launcher materials consumption'],
    [['explosive', 'attack'], 'Explosive attack'],
    [['explosive', 'damage'], 'Explosive damage'],
    [['explosive', 'radius'], 'Explosive radius'],
    [['explosive', 'blast'], 'Explosive blast'],
]).mapKeys(path => List(path))

export const statsInfo = Map().mapKeys(path => List(path))

export const negativeStats = Set([
    ['melee', 'consumption', 'energy'],
    ['melee', 'consumption', 'materials'],
    ['ranged', 'consumption', 'energy'],
    ['ranged', 'consumption', 'materials'],
    ['launcher', 'consumption', 'energy'],
    ['launcher', 'consumption', 'materials'],
]).map(path => List(path))

export const optionStats = Set([
    ['melee', 'stance'],
    ['ranged', 'stance'],
    ['launcher', 'stance'],
]).map(path => List(path))
