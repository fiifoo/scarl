import { List, Map } from 'immutable'
import React from 'react'
import SelectRow from '../form/SelectRow.jsx'
import Models from '../../data/Models'

const specs = {
    creature: {
        label: 'Creature',
        model: 'CreatureKind',
    },
    items: {
        label: 'Items',
        model: 'ItemKind',
        multi: true,
    },
    terrain: {
        label: 'Terrain',
        model: 'TerrainKind',
    },
    wall: {
        label: 'Wall',
        model: 'WallKind',
    },
}

const choices = Map(specs).map((spec, property) => ({
    value: property,
    label: spec.label,
})).toArray()

const EditorBrush = ({common, brush, setBrush}) => {
    const {data, models} = common

    const setProperty = property => setBrush(brush.set('property', property).set('value', null))
    const setValue = value => setBrush(brush.set('value', value))
    const setMultiValue = value => setBrush(brush.set('value', List(value)))

    const renderValueSelect = () => {
        const spec = specs[brush.property]

        return (
            <SelectRow
                label={spec.label}
                placeholder="Erase"
                choices={Models.choices(models, data, spec.model)}
                value={spec.multi ? (brush.value ? brush.value.toArray() : []) : brush.value}
                onChange={spec.multi ? setMultiValue : setValue}
                multi={spec.multi} />
        )
    }

    return (
        <div>
            <SelectRow
                label="Type"
                choices={choices}
                value={brush.property}
                onChange={setProperty} />
            {brush.property && renderValueSelect()}
        </div>
    )
}

export default EditorBrush
