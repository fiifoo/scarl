import React from 'react'
import Select from 'react-select'

const sort = (a, b) => a < b ? -1 : 1

const getValue = selection => selection ? selection.value : null

const options = items => items.map((_, id) => id).sort(sort).map(id => ({
    value: id,
    label: id
})).toArray()

const ItemSelect = ({item, items, selectItem}) => (
    <Select
        autoFocus
        value={item}
        onChange={selection => selectItem(getValue(selection))}
        options={options(items)} />
)

export default ItemSelect
