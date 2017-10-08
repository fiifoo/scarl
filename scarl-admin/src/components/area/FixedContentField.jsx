import React from 'react'
import FilteredFormField from '../field/FilteredFormField.jsx'

const formProperties = ['machinery']

const FixedContentField = props =>  (
    <FilteredFormField properties={formProperties} {...props} />
)

export default FixedContentField
