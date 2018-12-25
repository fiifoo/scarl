import { Set } from 'immutable'
import React from 'react'
import FormField from './FormField.jsx'

const getFilteredModel = (properties, model, exclude) => ({
    ...model,
    properties: model.properties.filter(property => {
        const contains = Set(properties).contains(property.name)

        return exclude ? ! contains : contains
    })
})

const FilteredFormField = ({properties, model, exclude = false, ...props}) => (
    <FormField
        model={getFilteredModel(properties, model, exclude)}
        {...props} />
)

export default FilteredFormField
