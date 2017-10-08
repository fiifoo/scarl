import React from 'react'
import SelectRow from '../form/SelectRow.jsx'
import Models from '../../data/Models'

const RelationField = ({label, path, value, fieldType, common}) => {
    const {data, models, setValue} = common

    const choices = Models.choices(models, data, fieldType.data.model)

    return (
        <SelectRow label={label} value={value} onChange={value => setValue(path, value)} choices={choices} />
    )
}

export default RelationField
