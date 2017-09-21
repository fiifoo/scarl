import React from 'react'
import TextInputRow from '../form/TextInputRow.jsx'

const StringField = ({label, path, value, setValue}) => (
    <TextInputRow label={label} value={value} onChange={value => setValue(path, value)} />
)

export default StringField
