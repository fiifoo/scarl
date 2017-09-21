import React from 'react'
import FormRow from './FormRow.jsx'

const ReadonlyRow = ({value, ...props}) => (
    <FormRow {...props}>
        <div className="form-control-static">{value}</div>
    </FormRow>
)

export default ReadonlyRow
