import React from 'react'
import FormRow from './FormRow.jsx'

const BooleanSelectRow = ({name, value, onChange, ...props}) => (
    <FormRow {...props}>
        <div className="boolean-select">
            <label className="radio-inline">
                <input type="radio" name={name} value="1" checked={!! value} onChange={() => onChange(true)} />
                Yes
            </label>
            <label className="radio-inline">
                <input type="radio" name={name} value="0" checked={! value} onChange={() => onChange(false)} />
                No
            </label>
        </div>
    </FormRow>
)

export default BooleanSelectRow
