import React from 'react'
import Select from 'react-select'
import FormRow from './FormRow.jsx'

const sort = (a, b) => a.label < b.label ? -1 : 1

const getValue = selection => selection ? selection.value : null

const SelectRow = ({choices, value, onChange, inputStyle, ...props}) => (
    <FormRow {...props}>
        <Select
            style={inputStyle}
            value={value}
            onChange={selection => onChange(getValue(selection))}
            options={choices.sort(sort)} />
    </FormRow>
)

export default SelectRow
