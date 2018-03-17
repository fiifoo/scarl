import { Map, Record } from 'immutable'

const CombatPower = Record({
    average: Map(),
    opposed: Map(),
    equipment: Map(),
})
CombatPower.read = raw => CombatPower({
    average: Map(raw.average),
    opposed: Map(raw.opposed).map(opponents => Map(opponents)),
    equipment: Map(raw.equipment).map(Map),
})

const Summary = Record({
    combatPower: CombatPower(),
    templates: Map(),
    valid: true,
})
Summary.read = raw => Summary({
    combatPower: CombatPower.read(raw.combatPower),
    templates: Map(raw.templates).map(Map),
    valid: raw.valid,
})

export default Summary
