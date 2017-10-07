import React from 'react'
import BooleanSelectRow from '../form/BooleanSelectRow.jsx'

const BooleanField = ({name, label, path, value, common}) => {
    const {setValue} = common

    const onChange = value => setValue(path, value)

    return (
        <BooleanSelectRow name={name} label={label} value={value} onChange={onChange} />
    )
}

export default BooleanField
