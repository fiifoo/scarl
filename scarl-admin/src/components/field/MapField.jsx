import { List } from 'immutable'
import React from 'react'
import FormRow from '../form/FormRow.jsx'
import { getFieldComponent, getFieldModel } from './utils'

const MapField = ({label, fieldType, path, value, common}) => {
    const {models, setValue} = common

    if (! value) {
        value = List()
    }

    const keyFieldType = fieldType.data.key
    const keyModel = getFieldModel(keyFieldType, models)
    const KeyComponent = getFieldComponent(keyFieldType, keyModel)

    const valueFieldType = fieldType.data.value
    const valueModel = getFieldModel(valueFieldType, models)
    const ValueComponent = getFieldComponent(valueFieldType, valueModel)

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
                    common={common} />
                <ValueComponent
                    label="value"
                    model={valueModel}
                    fieldType={valueFieldType}
                    path={valuePath}
                    value={valueValue}
                    common={common} />
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
