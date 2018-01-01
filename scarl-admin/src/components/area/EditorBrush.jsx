import { List, Map } from 'immutable'
import React from 'react'
import FormRow from '../form/FormRow.jsx'
import SelectRow from '../form/SelectRow.jsx'
import LocationContent from '../../data/area/LocationContent'
import Models from '../../data/Models'

const emptyContent = LocationContent()

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
    widget: {
        label: 'Widget',
        model: 'WidgetKind',
    },
}

const choices = Map(specs).map((spec, property) => ({
    value: property,
    label: spec.label,
})).toArray()

const EditorBrush = ({common, contents, editor, setBrush, setContents}) => {
    const {data, models} = common
    const {brush, locations} = editor

    const setProperty = property => setBrush(brush.set('property', property).set('value', null))
    const setValue = value => setBrush(brush.set('value', value))
    const setMultiValue = value => setBrush(brush.set('value', List(value)))

    const paint = () => {
        const next = Map(locations.toArray().map(location => {
            const next = contents.get(location, emptyContent).set(brush.property, brush.value)

            return [location, next]
        }))

        setContents(next)
    }

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

    const renderPaintButton = () => (
        <FormRow label="">
            <button type="button" className="btn btn-primary" onClick={paint}>
                Paint
            </button>
        </FormRow>
    )

    return (
        <div>
            <SelectRow
                label="Type"
                choices={choices}
                value={brush.property}
                onChange={setProperty} />
            {brush.property && renderValueSelect()}
            {brush.property && renderPaintButton()}
        </div>
    )
}

export default EditorBrush
