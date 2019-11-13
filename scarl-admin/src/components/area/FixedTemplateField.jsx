import React from 'react'
import { Nav, NavItem } from 'react-bootstrap'
import FilteredFormField from '../field/FilteredFormField.jsx'
import FormRow from '../form/FormRow.jsx'
import AreaEditorContainer from './AreaEditorContainer'

const formProperties = ['shape', 'theme', 'owner', 'power', 'terrain', 'features', 'content']

const TAB_FORM = 1
const TAB_EDITOR = 2

const FixedTemplateField = ({
    areaEditorVisible, setAreaEditorVisible,
    model, fieldType, path, value, common,
}) => {
    const tab = areaEditorVisible ? TAB_EDITOR : TAB_FORM
    const changeTab = tab => setAreaEditorVisible(tab === TAB_EDITOR)

    return (
        <div>
            <FormRow label={null}>
                <Nav bsStyle="pills" activeKey={tab} onSelect={changeTab} style={{marginTop: '1em', marginBottom: '1em'}}>
                    <NavItem eventKey={TAB_FORM}>
                        Values
                    </NavItem>
                    <NavItem eventKey={TAB_EDITOR}>
                        Editor
                    </NavItem>
                </Nav>
            </FormRow>

            {tab === TAB_FORM && (
                <FilteredFormField
                    properties={formProperties}
                    required={true}
                    model={model}
                    fieldType={fieldType}
                    path={path}
                    value={value}
                    common={common} />
            )}

            {tab === TAB_EDITOR && (
                <AreaEditorContainer
                    path={path}
                    value={value}
                    common={common} />
            )}
        </div>
    )
}

export default FixedTemplateField
