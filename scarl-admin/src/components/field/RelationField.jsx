import React from 'react'
import SelectRow from '../form/SelectRow.jsx'

const RelationField = ({label, path, value, fieldType, common}) => {
    const {data, models, setValue} = common

    const targetModel = models.main.get(fieldType.data.model)
    const targetItems = data.getIn(targetModel.dataPath)
    const choices = targetItems.map((item, id) => ({
        value: id,
        label: id
    })).toArray()

    return (
        <SelectRow label={label} value={value} onChange={value => setValue(path, value)} choices={choices} />
    )
}

export default RelationField
