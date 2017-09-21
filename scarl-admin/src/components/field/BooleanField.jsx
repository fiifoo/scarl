import React from 'react'
import BooleanSelectRow from '../form/BooleanSelectRow.jsx'

const BooleanField = ({name, label, path, value, setValue}) => (
    <BooleanSelectRow name={name} label={label} value={value} onChange={value => setValue(path, value)} />
)

export default BooleanField
