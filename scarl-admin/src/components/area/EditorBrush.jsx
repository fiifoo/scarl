import { Map } from 'immutable'
import React from 'react'
import LocationContent from '../../data/area/LocationContent'
import FormRow from '../form/FormRow.jsx'
import SelectRow from '../form/SelectRow.jsx'
import EditorLocationField from './EditorLocationField.jsx'

const emptyContent = LocationContent()

const fields = {
    creature: 'Creature',
    items: 'Items',
    terrain: 'Terrain',
    wall: 'Wall',
    widget: 'Widget',
    machineryControls: 'Machinery controls',
    machineryTargets: 'Machinery targets',
}

const choices = Map(fields).map((label, property) => ({
    value: property,
    label: label,
})).toArray()

const EditorBrush = ({common, machinery, contents, editor, setBrush, setContents}) => {
    const {brush, locations} = editor

    const setProperty = property => setBrush(brush.set('property', property).set('value', null))
    const setValue = value => setBrush(brush.set('value', value))

    const paint = () => {
        const next = Map(locations.toArray().map(location => {
            const next = contents.get(location, emptyContent).set(brush.property, brush.value)

            return [location, next]
        }))

        setContents(next)
    }

    const renderValueSelect = () => (
        <EditorLocationField
            property={brush.property}
            value={brush.value}
            setValue={setValue}
            machinery={machinery}
            common={common} />
    )

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
