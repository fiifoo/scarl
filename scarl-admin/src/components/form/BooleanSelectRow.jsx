import React from 'react'
import FormRow from './FormRow.jsx'

const BooleanSelectRow = ({name, value, onChange, disabled = false, required = false,...props}) => (
    <FormRow {...props} error={required && (value === null || value === undefined)}>
        <div className="boolean-select">
            <label className="radio-inline">
                <input
                    type="radio"
                    name={name}
                    value="1"
                    checked={!! value}
                    onChange={() => onChange(true)}
                    disabled={disabled} />
                Yes
            </label>
            <label className="radio-inline">
                <input
                    type="radio"
                    name={name}
                    value="0"
                    checked={! value}
                    onChange={() => onChange(false)}
                    disabled={disabled} />
                No
            </label>
        </div>
    </FormRow>
)

export default BooleanSelectRow
