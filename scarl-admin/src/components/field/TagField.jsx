import React from 'react'
import SelectRow from '../form/SelectRow.jsx'

const TagField = ({
    label, required, path, value, common,
}) => {
    const {horizontal, tags, setValue, addTag} = common

    const onChange = value => {
        if (value) {
            addTag(value)
        }
        setValue(path, value)
    }

    const choices = tags.toArray().map(tag => ({
        value: tag,
        label: tag
    }))

    return (
        <SelectRow
            creatable
            choices={choices}
            horizontal={horizontal}
            label={label}
            required={required}
            value={value}
            onChange={onChange} />
    )
}

export default TagField
