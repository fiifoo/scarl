import React from 'react'
import BooleanSelectRow from '../form/BooleanSelectRow.jsx'

const BooleanField = ({name, label, required, path, value, common}) => {
    const {setValue} = common

    const onChange = value => setValue(path, value)

    return (
        <BooleanSelectRow
            name={name}
            label={label}
            required={required}
            value={value}
            onChange={onChange} />
    )
}

export default BooleanField
