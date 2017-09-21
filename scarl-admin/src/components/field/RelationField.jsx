import React from 'react'
import SelectRow from '../form/SelectRow.jsx'

const RelationField = ({fieldType, path, setValue, data, models, ...props}) => {
    const targetModel = models.main.get(fieldType.data.model)
    const targetItems = data.getIn(targetModel.dataPath)
    const choices = targetItems.map((item, id) => ({
        value: id,
        label: id
    })).toArray()

    return (
        <SelectRow choices={choices} onChange={value => setValue(path, value)} {...props} />
    )
}

export default RelationField
