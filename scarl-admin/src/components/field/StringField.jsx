import React from 'react'
import TextInputRow from '../form/TextInputRow.jsx'

const StringField = ({label, required, path, value, common}) => {
    const {setValue} = common

    const onChange = value => setValue(path, value)

    return (
        <TextInputRow
            label={label}
            required={required}
            value={value}
            onChange={onChange} />
    )
}

export default StringField
