import React from 'react'
import { isPolymorphic, readItemId } from '../data/utils'
import { createFormFieldType, getFieldComponent } from './field/utils'
import ReadonlyRow from './form/ReadonlyRow.jsx'
import SideForm from './SideForm.jsx'

const ItemForm = ({item, model, sideForm, data, models, setItemValue, showSideForm, hideSideForm}) => {
    const submit = event => {
        event.preventDefault()

        return false
    }

    const id = readItemId(model, item)
    const path = model.dataPath.concat([id])
    const label = isPolymorphic(model) ? 'type' : undefined

    const fieldType = createFormFieldType(model)
    const common = {
        data,
        models,
        setValue: setItemValue,
        showSideForm,
        hideSideForm,
    }
    const Component = getFieldComponent(fieldType, model)

    return  (
        <form onSubmit={submit} className="form-horizontal item-form">
            <ReadonlyRow label="id" value={id} />
            <Component
                label={label}
                required={true}
                model={model}
                fieldType={fieldType}
                path={path}
                value={item}
                common={common} />
            <SideForm
                sideForm={sideForm}
                common={common} />
        </form>
    )
}

export default ItemForm
