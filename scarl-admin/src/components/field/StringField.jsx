import React from 'react'
import TextInputRow from '../form/TextInputRow.jsx'

const StringField = ({label, path, value, common}) => {
    const {setValue} = common

    const onChange = value => setValue(path, value)

    return (
        <TextInputRow label={label} value={value} onChange={onChange} />
    )
}

export default StringField
