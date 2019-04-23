import React from 'react'
import FormRow from './FormRow.jsx'
import { getValue, normalizeValue } from './utils.js'

const TextAreaRow = ({
    value, onChange, onFocus, onBlur,
    disabled = false, required = false,
    inputStyle, rows = 6,
    children,
    ...props
}) => {
    const input = (
        <textarea
            className="form-control"
            style={inputStyle}
            rows={rows}
            value={normalizeValue(value)}
            onChange={event => onChange(getValue(event))}
            onFocus={onFocus}
            onBlur={onBlur}
            disabled={disabled} />
    )

    return (
        <FormRow {...props} error={required && (value === null || value === undefined)}>
            {input}
            {children}
        </FormRow>
    )
}

export default TextAreaRow
