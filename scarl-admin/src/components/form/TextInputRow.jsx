import React from 'react'
import FormRow from './FormRow.jsx'
import { getValue, normalizeValue } from './utils.js'

const TextInputRow = ({value, onChange, inputStyle, inputType = 'text', disabled = false, required = false, ...props}) => (
    <FormRow {...props} error={required && (value === null || value === undefined)}>
        <input
            className="form-control"
            type={inputType}
            style={inputStyle}
            value={normalizeValue(value)}
            onChange={event => onChange(getValue(event))}
            disabled={disabled} />
    </FormRow>
)

export default TextInputRow
