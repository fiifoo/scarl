import { Map } from 'immutable'
import React from 'react'
import FormRow from '../form/FormRow.jsx'
import SelectRow from '../form/SelectRow.jsx'
import { createFormFieldType, getFieldComponent } from './utils'

const PolymorphicFormField = ({label, required, model, path, value, common}) => {
    const {models, setValue} = common

    if (! value && ! required) {
        return (
            <FormRow label={label}>
                <button type="button" className="btn btn-success" onClick={() => setValue(path, Map())}>Add</button>
            </FormRow>
        )
    }

    const type = value && value.get('type')
    const setType = value => setValue(path.concat(['type']), value)
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
                <button type="button" className="btn btn-danger delete-field" onClick={() => setValue(path, null)}>Remove</button>
            )}
            <SelectRow label={label} choices={typeChoices} value={type} onChange={setType} />
            {! type ? <div /> : renderForm()}
        </div>
    )
}

export default PolymorphicFormField
