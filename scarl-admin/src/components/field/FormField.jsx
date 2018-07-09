import React from 'react'
import { getNewValue } from '../../data/utils'
import FormRow from '../form/FormRow.jsx'
import { getFieldComponent, getFieldModel } from './utils'

const FormField = ({label, required, model, fieldType, path, value, common}) => {
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

    const renderSubField = subProperty => {
        const subFieldType = subProperty.fieldType
        const subModel = getFieldModel(subFieldType, models)
        const SubComponent = getFieldComponent(subFieldType, subModel)
        const subPath = path.concat([subProperty.name])
        const subValue = value && value.get(subProperty.name)

        return (
            <SubComponent
                key={subProperty.name}
                name={subProperty.name}
                label={subProperty.name}
                required={subFieldType.data.required}
                model={subModel}
                fieldType={subFieldType}
                path={subPath}
                value={subValue}
                common={common} />
        )
    }

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
            {model.properties.map(renderSubField)}
        </FormRow>
    )
}

export default FormField
