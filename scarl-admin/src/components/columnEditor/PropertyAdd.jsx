import React from 'react'
import { getColumnEditorProperties } from './utils'
import SelectRow from '../form/SelectRow.jsx'

const PropertyAdd = ({models, ui, setProperties}) => {
    const model = models.main.get(ui.model)
    const properties = getColumnEditorProperties(models)(model)
    const addProperty = property => setProperties(ui.properties.push(property))

    const filterProperty = property => ! ui.properties.contains(property)
    const createChoice = property => ({
        value: property,
        label: property.path.join('/'),
    })

    const choices = properties.filter(filterProperty).map(createChoice).toArray()

    return (
        <SelectRow
            label="Add property"
            input={{closeOnSelect: false, onSelectResetsInput: false}}
            choices={choices}
            value={null}
            onChange={addProperty} />
    )
}

export default PropertyAdd
