import { Map, Record } from 'immutable'

const CombatPower = Record({
    average: Map(),
    opposed: Map(),
})
CombatPower.read = raw => CombatPower({
    average: Map(raw.average),
    opposed: Map(raw.opposed).map(opponents => Map(opponents)),
})

const Summary = Record({
    valid: true,
    combatPower: CombatPower(),
    templates: Map(),
})
Summary.read = raw => Summary({
    valid: raw.valid,
    combatPower: CombatPower.read(raw.combatPower),
    templates: Map(raw.templates),
})

export default Summary
