import { is, List, Set } from 'immutable'
import React from 'react'
import { Friendly, Hostile } from '../../data/faction/dispositions'
import FilteredFormField from '../field/FilteredFormField.jsx'
import SelectRow from '../form/SelectRow.jsx'
import { createComponent } from '../utils.jsx'

const Disposition = props => {
    const {disposition, label} = props
    const {value, common} = props
    const {data, setValue} = common

    const factions = data.get('factions')
    const faction = value
    const factionId = faction.get('id')
    const dispositions = faction.get('dispositions')

    const selections = dispositions.filter(x => x.get(1) === disposition).map(x => x.get(0)).toSet()
    const choices = factions.keySeq().filter(x => x !== factionId).map(x => ({
        value: x,
        label: x,
    })).toArray()

    const add = (a, b) => factions => {
        const path = [a, 'dispositions']
        const value = List([b, disposition])

        const current = factions.getIn(path)
        const next = current.push(value)

        return factions.setIn(path, next)
    }

    const addFaction = otherId => {
        const next = add(factionId, otherId)(add(otherId, factionId)(factions))

        setValue(['factions'], next)
    }

    const remove = (a, b) => factions => {
        const path = [a, 'dispositions']
        const value = List([b, disposition])

        const current = factions.getIn(path)
        const next = current.filter(x => ! is(x, value))

        return factions.setIn(path, next)
    }

    const removeFaction = otherId => {
        const next = remove(factionId, otherId)(remove(otherId, factionId)(factions))

        setValue(['factions'], next)
    }

    const onChange = x => {
        const next = Set(x)

        next.subtract(selections).forEach(addFaction)
        selections.subtract(next).forEach(removeFaction)
    }

    return (
        <SelectRow
            label={label}
            multi={true}
            value={selections.toArray().sort()}
            choices={choices}
            onChange={onChange} />
    )
}

const Dispositions = props => {
    return (
        <div>
            <Disposition disposition={Hostile} label={'Enemies'} {...props} />
            <Disposition disposition={Friendly} label={'Friendlies'} {...props} />
        </div>
    )
}

const FactionField = createComponent(FilteredFormField, {
    properties: ['dispositions'],
    exclude: true,
    Extras: Dispositions,
})

export default FactionField
