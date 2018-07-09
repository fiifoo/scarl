import React from 'react'
import { getNewValue } from '../../data/utils'
import FormRow from '../form/FormRow.jsx'

const SideFormField = ({label, required, model, fieldType, path, value, common}) => {
    const {horizontal, models, setValue, showSideForm} = common

    if (! value) {
        return (
            <FormRow label={label} error={required} horizontal={horizontal}>
                <button
                    type="button"
                    className="btn btn-success"
                    disabled={! showSideForm}
                    onClick={() => {
                        setValue(path, getNewValue(fieldType, models))
                        showSideForm(model, fieldType, path)
                    }}>
                    Add
                </button>
            </FormRow>
        )
    }

    const show = () => showSideForm(model, fieldType, path)

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
            <button
                type="button"
                className="btn btn-info"
                onClick={show}
                disabled={! showSideForm}>Show</button>
        </FormRow>
    )
}

export default SideFormField
