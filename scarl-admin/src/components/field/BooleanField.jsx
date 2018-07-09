import React from 'react'
import BooleanSelectRow from '../form/BooleanSelectRow.jsx'

const BooleanField = ({name, label, required, path, value, common}) => {
    const {horizontal, setValue} = common

    const onChange = value => setValue(path, value)

    return (
        <BooleanSelectRow
            horizontal={horizontal}
            name={name}
            label={label}
            required={required}
            value={value}
            onChange={onChange} />
    )
}

export default BooleanField
