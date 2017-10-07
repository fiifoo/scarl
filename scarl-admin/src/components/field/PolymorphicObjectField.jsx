import React from 'react'
import SelectRow from '../form/SelectRow.jsx'

const PolymorphicObjectField = ({label, model, path, value, common}) => {
    const {setValue} = common

    const choices = model.polymorphic.map(type => ({value: type, label: type}))

    return (
        <SelectRow label={label} choices={choices} value={value} onChange={value => setValue(path, value)} />
    )
}

export default PolymorphicObjectField
