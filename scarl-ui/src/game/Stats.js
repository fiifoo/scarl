import { fromJS, List, OrderedMap, Record, Set } from 'immutable'
import { getConditionLabel } from './condition'
import { getStanceLabel } from './stance'

const stats = OrderedMap([
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
    [['melee', 'stance'], 'Melee required stance'],
    [['melee', 'conditions'], 'Melee caused conditions'],
    [['melee', 'consumption', 'energy'], 'Melee energy consumption'],
    [['melee', 'consumption', 'materials'], 'Melee materials consumption'],
    [['ranged', 'attack'], 'Ranged attack'],
    [['ranged', 'damage'], 'Ranged damage'],
    [['ranged', 'range'], 'Ranged attack range'],
    [['ranged', 'stance'], 'Ranged required stance'],
    [['ranged', 'conditions'], 'Ranged caused conditions'],
    [['ranged', 'consumption', 'energy'], 'Ranged energy consumption'],
    [['ranged', 'consumption', 'materials'], 'Ranged materials consumption'],
    [['launcher', 'range'], 'Launcher attack range'],
    [['launcher', 'stance'], 'Launcher required stance'],
    [['launcher', 'consumption', 'energy'], 'Launcher energy consumption'],
    [['launcher', 'consumption', 'materials'], 'Launcher materials consumption'],
    [['explosive', 'attack'], 'Explosive attack'],
    [['explosive', 'damage'], 'Explosive damage'],
    [['explosive', 'radius'], 'Explosive radius'],
    [['explosive', 'blast'], 'Explosive blast'],
    [['explosive', 'conditions'], 'Explosive caused conditions'],
    [['stances'], 'Stances'],
]).mapKeys(path => List(path))

const negativeStats = Set([
    ['melee', 'consumption', 'energy'],
    ['melee', 'consumption', 'materials'],
    ['ranged', 'consumption', 'energy'],
    ['ranged', 'consumption', 'materials'],
    ['launcher', 'consumption', 'energy'],
    ['launcher', 'consumption', 'materials'],
]).map(path => List(path))

const listStats = Set([
    ['melee', 'conditions'],
    ['ranged', 'conditions'],
    ['explosive', 'conditions'],
    ['stances'],
]).map(path => List(path))

const optionStats = Set([
    ['melee', 'stance'],
    ['ranged', 'stance'],
    ['launcher', 'stance'],
]).map(path => List(path))

const conditionStat = Set([
    ['melee', 'conditions'],
    ['ranged', 'conditions'],
    ['explosive', 'conditions'],
]).map(path => List(path))

const stanceStat = Set([
    ['melee', 'stance'],
    ['ranged', 'stance'],
    ['launcher', 'stance'],
    ['stances'],
]).map(path => List(path))

const addStats = (a, b) => {
    a = fromJS(a)
    b = fromJS(b)

    return stats.reduce((a, _, stat) => (
        a.setIn(stat, addStat(stat, a, b))
    ), a).toJS()

}

const addStat = (stat, a, b) => {
    if (listStats.contains(stat)) {
        return a.getIn(stat).concat(b.getIn(stat))
    } else if (optionStats.contains(stat)) {
        return a.getIn(stat, b.getIn(stat))
    } else {
        return a.getIn(stat) + b.getIn(stat)
    }
}

const getSingleDisplayValue = (stat, signed = false) => value => {
    if (conditionStat.contains(stat)) {
        return `${getConditionLabel(value.get('type'))} (${value.getIn(['data', 'strength'])})`
    } else if (stanceStat.contains(stat)) {
        return getStanceLabel(value)
    } else {
        return (signed && value > 0 ? '+' : '') + value
    }
}

const getDisplayValue = stat => value => {
    const getSingle = getSingleDisplayValue(stat)

    if (listStats.contains(stat)) {
        return value == null || value.isEmpty() ? 'None' : value.map(getSingle).join(', ')
    } else if (optionStats.contains(stat)) {
        return value ? getSingle(value) : 'None'
    } else {
        return getSingle(value)
    }
}

const isEmpty = stat => value => {
    if (listStats.contains(stat)) {
        return value == null || value.isEmpty()
    } else if (optionStats.contains(stat)) {
        return ! value
    } else {
        return value === 0
    }
}

const isVerbose = stat => listStats.contains(stat) || optionStats.contains(stat)

const Diff = Record({
    displayValue: undefined,
    positive: undefined,
})
Diff.create = stat => (displayValue, positive) => Diff({
    displayValue,
    positive: negativeStats.contains(stat) ? ! positive : positive
})

const diff = stat => (value, compareValue) => {
    const create = Diff.create(stat)
    const display = getSingleDisplayValue(stat, true)

    if (listStats.contains(stat)) {
        value = value == null ? List() : value
        compareValue = compareValue == null ? List() : compareValue

        const negative = compareValue.filter(x => value.indexOf(x) === -1).map(x => create(display(x), false))
        const positive = value.filter(x => compareValue.indexOf(x) === -1).map(x => create(display(x), true))

        return negative.concat(positive)
    } else if (optionStats.contains(stat)) {
        if (value === compareValue) {
            return List()
        } else {
            return List([
                compareValue == null ? null : create(display(compareValue), false),
                value == null ? null : create(display(value), true),
            ]).filter(x => x !== null)
        }
    } else {
        const amount = value - (compareValue || 0)

        if (amount === 0) {
            return List()
        } else {
            return List([create(display(amount), amount >= 0)])
        }
    }
}

const Stats = {
    stats,

    add: addStats,
    diff,
    getDisplayValue,
    isEmpty,
    isVerbose,
}

export default Stats
