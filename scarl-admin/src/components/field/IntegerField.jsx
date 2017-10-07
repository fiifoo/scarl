import React from 'react'
import TextInputRow from '../form/TextInputRow.jsx'

const IntegerField = ({label, path, value, common}) => {
    const {setValue} = common

    const onChange = value => {
        if (value === null) {
            setValue(path, null)
        } else if (value.match(/^\d*$/)) {
            setValue(path, parseInt(value, 10))
        }
    }

    return (
        <TextInputRow label={label} value={value} onChange={onChange} />
    )
}

export default IntegerField
