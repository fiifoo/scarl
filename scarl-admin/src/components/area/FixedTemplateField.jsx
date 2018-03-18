import React from 'react'
import FilteredFormField from '../field/FilteredFormField.jsx'
import FormRow from '../form/FormRow.jsx'
import EditorContainer from '../../containers/EditorContainer'

const formProperties = ['shape', 'category', 'power', 'features', 'content']

const FixedTemplateField = ({model, fieldType, path, value, common}) =>  (
    <div>
        <FormRow>
            <FilteredFormField
                properties={formProperties}
                required={true}
                model={model}
                fieldType={fieldType}
                path={path}
                value={value}
                common={common} />
        </FormRow>
        <hr />
        <EditorContainer
            path={path}
            value={value}
            common={common} />
    </div>
)

export default FixedTemplateField
