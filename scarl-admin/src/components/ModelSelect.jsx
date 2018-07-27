import React from 'react'
import Select from 'react-select'

const sort = (a, b) => a < b ? -1 : 1

const getValue = selection => selection ? selection.value : null

const options = models => models.main.map((_, id) => id).sort(sort).map(id => ({
    value: id,
    label: id
})).toArray()

const ModelSelect = ({model, models, selectModel}) => (
    <Select
        autoFocus
        value={model}
        onChange={selection => selectModel(getValue(selection))}
        options={options(models)} />
)

export default ModelSelect
