import { List } from 'immutable'
import React from 'react'
import SelectRow from '../form/SelectRow.jsx'

const PolymorphicObjectField = ({label, required, model, path, value, multi = false, common}) => {
    const {setValue} = common

    const choices = model.polymorphic.map(type => ({value: type, label: type}))

    if (multi) {
        value = value.toArray()
    }

    const onChange = multi ? (
        value => setValue(path, List(value))
    ) : (
        value => setValue(path, value)
    )

    return (
        <SelectRow
            label={label}
            required={required}
            choices={choices}
            value={value}
            onChange={onChange}
            multi={multi} />
    )
}

export default PolymorphicObjectField
