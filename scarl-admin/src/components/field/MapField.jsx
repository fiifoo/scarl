import { List } from 'immutable'
import React from 'react'
import FormRow from '../form/FormRow.jsx'
import { getFieldComponent, getFieldModel } from './utils'

const MapField = ({label, fieldType, path, value, setValue, data, models}) => {
    if (! value) {
        value = List()
    }

    const keyFieldType = fieldType.data.key
    const KeyComponent = getFieldComponent(keyFieldType)
    const keyModel = getFieldModel(keyFieldType, models)

    const valueFieldType = fieldType.data.value
    const ValueComponent = getFieldComponent(valueFieldType)
    const valueModel = getFieldModel(valueFieldType, models)

    const renderFields = (subValue, index) => {
        const keyPath = path.concat([index, 0])
        const keyValue = subValue.get(0)
        const valuePath = path.concat([index, 1])
        const valueValue = subValue.get(1)

        return (
            <div key={index}>
                <button type="button" className="btn btn-danger delete-field" onClick={() => setValue(path, value.remove(index))}>Remove</button>
                <KeyComponent
                    label="key"
                    model={keyModel}
                    fieldType={keyFieldType}
                    path={keyPath}
                    value={keyValue}
                    setValue={setValue}
                    data={data}
                    models={models} />
                <ValueComponent
                    label="value"
                    model={valueModel}
                    fieldType={valueFieldType}
                    path={valuePath}
                    value={valueValue}
                    setValue={setValue}
                    data={data}
                    models={models} />
            </div>
        )
    }

    return (
        <FormRow label={label}>
            {value.map(renderFields)}
            <button type="button" className="btn btn-success" onClick={() => setValue(path, value.push(List()))}>Add</button>
        </FormRow>
    )
}

export default MapField
