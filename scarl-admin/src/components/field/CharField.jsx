import React from 'react'
import TextInputRow from '../form/TextInputRow.jsx'

const CharField = ({label, path, value, setValue}) => {
    const onChange = value => {
        if (value === null || value.length <= 1) {
            setValue(path, value)
        }
    }

    return (
        <TextInputRow label={label} value={value} onChange={onChange} inputStyle={{width: 50}} />
    )
}

export default CharField
