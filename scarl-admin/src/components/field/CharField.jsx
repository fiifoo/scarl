import React from 'react'
import TextInputRow from '../form/TextInputRow.jsx'

const CharField = ({label, required, path, value, common}) => {
    const {horizontal, setValue} = common

    const onChange = value => {
        if (value === null || value.length <= 1) {
            setValue(path, value)
        }
    }

    return (
        <TextInputRow
            horizontal={horizontal}
            label={label}
            required={required}
            value={value}
            onChange={onChange}
            inputStyle={{width: 50}} />
    )
}

export default CharField
