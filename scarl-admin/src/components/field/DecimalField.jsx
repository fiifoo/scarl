import React from 'react'
import TextInputRow from '../form/TextInputRow.jsx'

const DecimalField = ({label, path, value, common}) => {
    const {setValue} = common

    const onChange = value => {
        if (value === null || value.match(/^\d*(\.\d*)?$/)) {
            setValue(path, value)
        }

        if (value === null) {
            setValue(path, null)
        } else if (value.match(/^\d+\.$/)) {
            setValue(path, value)
        } else if (value.match(/^\d+(\.\d*)?$/)) {
            setValue(path, parseFloat(value))
        }
    }

    return (
        <TextInputRow label={label} value={value} onChange={onChange} />
    )
}

export default DecimalField
