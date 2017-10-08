import React from 'react'
import FilteredFormField from '../field/FilteredFormField.jsx'

const formProperties = ['mechanism']

const MachinerySourceField = props =>  (
    <FilteredFormField properties={formProperties} {...props} />
)

export default MachinerySourceField
