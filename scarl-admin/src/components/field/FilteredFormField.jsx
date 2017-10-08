import { Set } from 'immutable'
import React from 'react'
import FormField from './FormField.jsx'

const getFilteredModel = (properties, model) => ({
    ...model,
    properties: model.properties.filter(property => Set(properties).contains(property.name))
})

const FilteredFormField = ({properties, model, ...props}) => (
    <FormField
        model={getFilteredModel(properties, model)}
        {...props} />
)

export default FilteredFormField
