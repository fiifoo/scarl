import { createComponent } from '../utils.jsx'
import FilteredFormField from '../field/FilteredFormField.jsx'

const WorldField = createComponent(FilteredFormField, {
    properties: ['conduits'],
    exclude: true,
})

export default WorldField
