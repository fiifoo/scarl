import { Map } from 'immutable'
import React from 'react'
import { isPolymorphic } from '../../data/utils.js'
import FormRow from '../form/FormRow.jsx'
import SelectRow from '../form/SelectRow.jsx'
import { getFieldComponent, getFieldModel } from './utils'

const FormField = props => (
    isPolymorphic(props.model) ? (
        props.model.objectPolymorphism ? (
            <PolymorphicObjectField {...props} />
        ) : (
            <PolymorphicFormField {...props} />
        )
    ) : (
        <NormalFormField {...props} />
    )
)

const NormalFormField = ({label, required, model, path, value, setValue, data, models}) => {
    if (! value && ! required) {
        return (
            <FormRow label={label}>
                <button type="button" className="btn btn-success" onClick={() => setValue(path, Map())}>Add</button>
            </FormRow>
        )
    }

    const renderSubField = subProperty => {
        const subFieldType = subProperty.fieldType
        const SubComponent = getFieldComponent(subFieldType)
        const subModel = getFieldModel(subFieldType, models)
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
                setValue={setValue}
                data={data}
                models={models} />
        )
    }

    return (
        <FormRow label={label}>
            {required ? null : (
                <button type="button" className="btn btn-danger delete-field" onClick={() => setValue(path, null)}>Remove</button>
            )}
            {model.properties.map(renderSubField)}
        </FormRow>
    )
}

const PolymorphicFormField = ({label, required, model, path, value, setValue, data, models}) => {
    if (! value && ! required) {
        return (
            <FormRow label={label}>
                <button type="button" className="btn btn-success" onClick={() => setValue(path, Map())}>Add</button>
            </FormRow>
        )
    }

    const type = value && value.get('type')
    const subModel = type && models.sub.get(type)
    const subValue = value && value.get('data')

    const setType = value => setValue(path.concat(['type']), value)
    const typeChoices = model.polymorphic.map(type => ({value: type, label: type}))

    return (
        <div>
            {required ? null : (
                <button type="button" className="btn btn-danger delete-field" onClick={() => setValue(path, null)}>Remove</button>
            )}
            <SelectRow label={label} choices={typeChoices} value={type} onChange={setType} />
            {! subModel || subModel.properties.length === 0 ? <div /> : (
                <FormField
                    required={true}
                    model={subModel}
                    path={path.concat(['data'])}
                    value={subValue}
                    setValue={setValue}
                    data={data}
                    models={models} />
            )}
        </div>
    )
}

const PolymorphicObjectField = ({label, model, path, value, setValue}) => {
    const choices = model.polymorphic.map(type => ({value: type, label: type}))

    return (
        <SelectRow label={label} choices={choices} value={value} onChange={value => setValue(path, value)} />
    )
}

export default FormField
