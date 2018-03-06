import { List } from 'immutable'
import React from 'react'
import { getNewValue, isPolymorphic } from '../../data/utils'
import FormRow from '../form/FormRow.jsx'
import PolymorphicObjectField from './PolymorphicObjectField.jsx'
import { getFieldComponent, getFieldModel } from './utils'

const ListField = ({value, ...props}) => {
    const {fieldType, common} = props
    const {models} = common

    if (! value) {
        value = List()
    }

    const valueFieldType = fieldType.data.value
    const valueModel = getFieldModel(valueFieldType, models)

    if (valueFieldType.type === 'FormField' && isPolymorphic(valueModel) && valueModel.objectPolymorphism) {
        return (
            <SelectComponent Component={PolymorphicObjectField} value={value} {...props} />
        )
    }

    return (
        <MultiComponent value={value} {...props} />
    )
}

const SelectComponent = ({Component, label, fieldType, path, value, common}) => {
    const {models} = common

    const valueFieldType = fieldType.data.value
    const valueModel = getFieldModel(valueFieldType, models)

    return (
        <Component
            label={label}
            required={valueFieldType.data.required}
            model={valueModel}
            fieldType={valueFieldType}
            path={path}
            value={value}
            multi={true}
            common={common} />
    )
}

const MultiComponent = ({label, fieldType, path, value, common}) => {
    const {models, setValue} = common

    const valueFieldType = fieldType.data.value
    const valueModel = getFieldModel(valueFieldType, models)
    const ValueComponent = getFieldComponent(valueFieldType, valueModel)

    const add = () => setValue(path, value.push(getNewValue(valueFieldType, models)))

    const renderValueField = (subValue, index) => {
        const valuePath = path.concat([index])

        return (
            <div key={index}>
                <button
                    type="button"
                    className="btn btn-danger delete-field"
                    onClick={() => setValue(path, value.remove(index))}>
                    Remove
                </button>
                <ValueComponent
                    required={valueFieldType.data.required}
                    model={valueModel}
                    fieldType={valueFieldType}
                    path={valuePath}
                    value={subValue}
                    common={common} />
            </div>
        )
    }

    return (
        <FormRow label={label}>
            {value.map(renderValueField)}
            <button
                type="button"
                className="btn btn-success"
                onClick={add}>
                Add
            </button>
        </FormRow>
    )
}


export default ListField
