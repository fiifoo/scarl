import React from 'react'
import { Nav, NavItem } from 'react-bootstrap'
import FilteredFormField from '../field/FilteredFormField.jsx'
import FormRow from '../form/FormRow.jsx'
import AreaEditorContainer from './AreaEditorContainer'

const formProperties = ['shape', 'power', 'terrain', 'features', 'content']

const TAB_FORM = 1
const TAB_EDITOR = 2

const PureFixedTemplateField = ({
    tab, changeTab,
    model, fieldType, path, value, common,
}) =>  (
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

class FixedTemplateField extends React.Component {

    constructor(props) {
        super(props)

        this.state = {tab: TAB_FORM}

        this.changeTab = tab => {
            this.setState({tab})
        }
    }

    render() {
        return (
            <PureFixedTemplateField
                changeTab={this.changeTab}
                {...this.props}
                {...this.state} />
        )
    }
}

export default FixedTemplateField
