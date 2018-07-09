import { Map } from 'immutable'
import React from 'react'
import { getNewValue } from '../../data/utils'
import FormRow from '../form/FormRow.jsx'
import SelectRow from '../form/SelectRow.jsx'
import { createFormFieldType, getFieldComponent } from './utils'

const PolymorphicFormField = ({label, required, model, fieldType, path, value, common}) => {
    const {horizontal, models, setValue} = common

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
    const setType = type => {
        const subFieldType = createFormFieldType(models.sub.get(type))
        const id = value.getIn(['data', 'id'])
        const data = id ? (
            getNewValue(subFieldType, models).set('id', id)
        ) : (
            getNewValue(subFieldType, models)
        )
        setValue(path, Map({type, data}))
    }
    const typeChoices = model.polymorphic.map(type => ({value: type, label: type}))

    const renderForm = () => {
        const subModel = models.sub.get(type)
        if (subModel.properties.length === 0) {
            return <div />
        }

        const subFieldType = createFormFieldType(subModel)
        const subValue = value && value.get('data')

        const Component = getFieldComponent(subFieldType, subModel)

        return (
            <Component
                required={true}
                model={subModel}
                fieldType={subFieldType}
                path={path.concat(['data'])}
                value={subValue}
                common={common} />
        )
    }

    return (
        <div>
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
                label={label}
                required={true}
                choices={typeChoices}
                value={type}
                onChange={setType} />
            {! type ? <div /> : renderForm()}
        </div>
    )
}

export default PolymorphicFormField
