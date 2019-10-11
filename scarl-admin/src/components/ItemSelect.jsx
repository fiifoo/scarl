import React from 'react'
import Select from 'react-select'
import { reactSelect } from './form/utils'

const sort = (a, b) => a < b ? -1 : 1

const getValue = selection => selection ? selection.value : null

const getSelection = value => ({
    value,
    label: value,
})

const options = items => items.map((_, id) => id).sort(sort).map(id => ({
    value: id,
    label: id
})).toArray()

const ItemSelect = ({item, items, selectItem}) => (
    <Select
        autoFocus
        value={item ? getSelection(item) : null}
        onChange={selection => selectItem(getValue(selection))}
        options={options(items)}
        filterOption={reactSelect.filterOption}
        styles={reactSelect.styles} />
)

export default ItemSelect
