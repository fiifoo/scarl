import React from 'react'
import TextAreaRow from '../form/TextAreaRow.jsx'

const TextField = ({label, required, path, value, common}) => {
    const {horizontal, setValue} = common

    const onChange = value => setValue(path, value)

    return (
        <TextAreaRow
            horizontal={horizontal}
            label={label}
            required={required}
            value={value}
            onChange={onChange} />
    )
}

export default TextField
