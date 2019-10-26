import React from 'react'
import Select from 'react-select'
import CreatableSelect from 'react-select/creatable'
import FormRow from './FormRow.jsx'
import { reactSelect } from './utils'

const sort = (a, b) => a.label < b.label ? -1 : 1

const getSingleValue = selection => (
    selection.value
)

const getValue = (multi, selection) => multi ? (
    selection == null ? [] : selection.map(getSingleValue)
) : (
    selection == null ? null : getSingleValue(selection)
)

const getSingleSelection = value => ({
    value,
    label: value,
})

const getSelection = (multi, value) => multi ? (
    value == null ? [] : value.map(getSingleSelection)
) : (
    value == null ? null : getSingleSelection(value)
)

const SelectRow = ({
    button, choices, value, onChange,
    multi = false, disabled = false, required = false, creatable = false,
    placeholder = undefined, input = {}, ...props
}) => {
    const Component = creatable ? CreatableSelect : Select

    const component = <Component
        value={getSelection(multi, value)}
        isMulti={multi}
        isClearable={! required}
        onChange={selection => onChange(getValue(multi, selection))}
        options={choices.sort(sort)}
        isDisabled={disabled}
        placeholder={placeholder}

        filterOption={reactSelect.filterOption}
        styles={reactSelect.styles}
        {...input} />

    return (
        <FormRow {...props} error={required && (value === null || value === undefined)}>
            {button ? (
                <div className="input-group">
                    {component}
                    <span className="input-group-btn">
                        {button}
                    </span>
                </div>
            ) : (
                component
            )}
        </FormRow>
    )
}

export default SelectRow
