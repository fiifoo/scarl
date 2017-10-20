import React from 'react'
import Select from 'react-select'
import FormRow from './FormRow.jsx'

const sort = (a, b) => a.label < b.label ? -1 : 1

const getValue = (multi, selection) => multi ? (
    selection.map(selection => selection.value)
) : (
    selection ? selection.value : null
)

const SelectRow = ({choices, value, onChange, inputStyle, multi = false, disabled = false, required = false, placeholder = undefined,  ...props}) => (
    <FormRow {...props} error={required && (value === null || value === undefined)}>
        <Select
            style={inputStyle}
            value={value}
            multi={multi}
            clearable={! required}
            onChange={selection => onChange(getValue(multi, selection))}
            options={choices.sort(sort)}
            disabled={disabled}
            placeholder={placeholder} />
    </FormRow>
)

export default SelectRow
