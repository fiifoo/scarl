import React from 'react'
import SelectRow from '../form/SelectRow.jsx'

const ModelSelect = ({models, ui, setModel}) => {
    const choices = models.main.map((_, id) => ({
        value: id,
        label: id,
    })).toArray()

    return (
        <SelectRow
            label="Select model"
            input={{autoFocus: true}}
            choices={choices}
            value={ui.model}
            onChange={setModel} />
    )
}

export default ModelSelect
