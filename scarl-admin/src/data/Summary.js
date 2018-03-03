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
    combatPower: CombatPower(),
    equipmentCombatPower: Map(),
    templates: Map(),
    valid: true,
})
Summary.read = raw => Summary({
    equipmentCombatPower: Map(raw.equipmentCombatPower).map(Map),
    combatPower: CombatPower.read(raw.combatPower),
    templates: Map(raw.templates),
    valid: raw.valid,
})

export default Summary
