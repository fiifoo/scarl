import React from 'react'
import FormRow from '../form/FormRow.jsx'
import SelectRow from '../form/SelectRow.jsx'

const PolymorphicRelationField = ({fieldType, label, required, path, value, setValue, data, models}) => {

    if (! value && ! required) {
        return (
            <FormRow label={label}>
                <button type="button" className="btn btn-success" onClick={() => setValue(path, Map())}>Add</button>
            </FormRow>
        )
    }

    const type = value && value.get('type')
    const targetModel = type && models.main.get(type)
    const targetValue = value && value.get('value')
    const targetItems = targetModel && data.getIn(targetModel.dataPath)
    const targetChoices = targetItems && targetItems.map((item, id) => ({
        value: id,
        label: id
    })).toArray()

    const setType = value => setValue(path.concat(['type']), value)
    const typeChoices = fieldType.data.models.map(type => ({value: type, label: type}))

    return (
        <FormRow label={label}>
            {required ? null : (
                <button type="button" className="btn btn-danger delete-field" onClick={() => setValue(path, null)}>Remove</button>
            )}
            <SelectRow label="type" choices={typeChoices} value={type} onChange={setType} />
            {! targetChoices ? <div /> : (
                <SelectRow label="value" choices={targetChoices} value={targetValue} onChange={value => setValue(path.concat(['value']), value)} />
            )}
        </FormRow>
    )
}

export default PolymorphicRelationField
