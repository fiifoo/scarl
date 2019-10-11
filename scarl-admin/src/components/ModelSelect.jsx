import React from 'react'
import Select from 'react-select'
import { reactSelect } from './form/utils'

const sort = (a, b) => a < b ? -1 : 1

const getValue = selection => selection ? selection.value : null

const getSelection = value => ({
    value,
    label: value,
})

const options = models => models.main.map((_, id) => id).sort(sort).map(id => ({
    value: id,
    label: id
})).toArray()

const ModelSelect = ({model, models, selectModel}) => (
    <Select
        autoFocus
        value={model ? getSelection(model) : null}
        onChange={selection => selectModel(getValue(selection))}
        options={options(models)}
        filterOption={reactSelect.filterOption}
        styles={reactSelect.styles} />
)

export default ModelSelect
