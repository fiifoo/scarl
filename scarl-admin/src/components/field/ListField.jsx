import { List } from 'immutable'
import React from 'react'
import FormRow from '../form/FormRow.jsx'
import { getFieldComponent, getFieldModel } from './utils'

const ListField = ({label, fieldType, path, value, setValue, data, models }) => {
    if (! value) {
        value = List()
    }

    const valueFieldType = fieldType.data.value
    const ValueComponent = getFieldComponent(valueFieldType)
    const valueModel = getFieldModel(valueFieldType, models)

    const renderValueField = (subValue, index) => {
        const valuePath = path.concat([index])

        return (
            <div key={index}>
                <button type="button" className="btn btn-danger delete-field" onClick={() => setValue(path, value.remove(index))}>Remove</button>
                <ValueComponent
                    model={valueModel}
                    fieldType={valueFieldType}
                    path={valuePath}
                    value={subValue}
                    setValue={setValue}
                    data={data}
                    models={models} />
            </div>
        )
    }

    return (
        <FormRow label={label}>
            {value.map(renderValueField)}
            <button type="button" className="btn btn-success" onClick={() => setValue(path, value.push(null))}>Add</button>
        </FormRow>
    )
}

export default ListField
