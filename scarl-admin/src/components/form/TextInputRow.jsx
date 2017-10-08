import React from 'react'
import FormRow from './FormRow.jsx'
import { getValue, normalizeValue } from './utils.js'

const TextInputRow = ({value, onChange, inputStyle, inputType = 'text', disabled = false, ...props}) => (
    <FormRow {...props}>
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
