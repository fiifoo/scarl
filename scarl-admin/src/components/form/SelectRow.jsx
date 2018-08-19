import React from 'react'
import Select from 'react-select'
import CreatableSelect from 'react-select/lib/Creatable'
import FormRow from './FormRow.jsx'

const sort = (a, b) => a.label < b.label ? -1 : 1

const getValue = (multi, selection) => multi ? (
    selection.map(selection => selection.value)
) : (
    selection ? selection.value : null
)

const SelectRow = ({
    button, choices, value, onChange,
    multi = false, disabled = false, required = false, creatable = false,
    inputStyle, placeholder = undefined, input = {}, ...props,
}) => {
    const Component = creatable ? CreatableSelect : Select

    const component = <Component
        style={inputStyle}
        value={value}
        multi={multi}
        clearable={! required}
        onChange={selection => onChange(getValue(multi, selection))}
        options={choices.sort(sort)}
        disabled={disabled}
        placeholder={placeholder}
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
