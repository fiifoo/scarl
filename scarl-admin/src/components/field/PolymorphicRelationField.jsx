import { Map } from 'immutable'
import React from 'react'
import { getNewValue } from '../../data/utils'
import FormRow from '../form/FormRow.jsx'
import SelectRow from '../form/SelectRow.jsx'

const PolymorphicRelationField = ({fieldType, label, required, path, value, common}) => {
    const {data, horizontal, models, setValue} = common

    if (! value) {
        return (
            <FormRow label={label} error={required} horizontal={horizontal}>
                <button
                    type="button"
                    className="btn btn-success"
                    onClick={() => setValue(path, getNewValue(fieldType, models))}>
                    Add
                </button>
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

    const setType = type => setValue(path, Map({type, data: null}))
    const typeChoices = fieldType.data.models.map(type => ({value: type, label: type}))

    const link = common.showItem && targetChoices && (
        <button
            type="button"
            className="btn btn-link"
            onClick={() => common.showItem(targetModel.id, targetValue)}
            disabled={!targetValue}>
            Show
        </button>
    )

    return (
        <FormRow label={label} horizontal={horizontal}>
            {required ? null : (
                <button
                    type="button"
                    className="btn btn-danger delete-field"
                    onClick={() => setValue(path, null)}>
                    Remove
                </button>
            )}
            <SelectRow
                horizontal={horizontal}
                label="type"
                required={true}
                choices={typeChoices}
                value={type}
                onChange={setType} />
            {! targetChoices ? <div /> : (
                <SelectRow
                    horizontal={horizontal}
                    label="value"
                    required={true}
                    choices={targetChoices}
                    value={targetValue}
                    onChange={value => setValue(path.concat(['value']), value)}
                    button={link} />
            )}
        </FormRow>
    )
}

export default PolymorphicRelationField
