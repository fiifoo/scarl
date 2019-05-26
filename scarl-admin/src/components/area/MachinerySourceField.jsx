import React from 'react'
import FilteredFormField from '../field/FilteredFormField.jsx'

const excludeProperties = ['controls', 'targets']

const MachinerySourceField = props =>  (
    <FilteredFormField properties={excludeProperties} exclude {...props} />
)

export default MachinerySourceField
