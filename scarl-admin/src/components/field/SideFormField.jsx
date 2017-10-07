import React from 'react'
import FormRow from '../form/FormRow.jsx'

const SideFormField = ({label, required, model, fieldType, path, value, common}) => {
    const {setValue, showSideForm} = common

    if (! value && ! required) {
        return (
            <FormRow label={label}>
                <button type="button" className="btn btn-success" onClick={() => setValue(path, Map())}>Add</button>
            </FormRow>
        )
    }

    const show = () => showSideForm(model, fieldType, path)

    return (
        <FormRow label={label}>
            {required ? null : (
                <button type="button" className="btn btn-danger delete-field" onClick={() => setValue(path, null)}>Remove</button>
            )}
            <button type="button" className="btn btn-info" onClick={show}>Show</button>
        </FormRow>
    )
}

export default SideFormField
