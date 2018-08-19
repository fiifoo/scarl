import React from 'react'
import FormRow from './FormRow.jsx'
import { getValue, normalizeValue } from './utils.js'

const TextInputRow = ({
    value, onChange, onFocus, onBlur,
    disabled = false, required = false,
    inputStyle, inputType = 'text',
    addon, children,
    ...props
}) => {
    const input = (
        <input
            className="form-control"
            type={inputType}
            style={inputStyle}
            value={normalizeValue(value)}
            onChange={event => onChange(getValue(event))}
            onFocus={onFocus}
            onBlur={onBlur}
            disabled={disabled} />
    )

    const content = ! addon ? input : (
        <div className="input-group">
            {input}
            <div className="input-group-addon">{addon}</div>
        </div>
    )

    return (
        <FormRow {...props} error={required && (value === null || value === undefined)}>
            {content}
            {children}
        </FormRow>
    )
}

export default TextInputRow
