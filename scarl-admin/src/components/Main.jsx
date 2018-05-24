import React from 'react'
import { Col, Nav, NavItem, Row, Tab } from 'react-bootstrap'
import ItemAddContainer from '../containers/ItemAddContainer'
import ItemFormContainer from '../containers/ItemFormContainer'
import ItemReferencesContainer from '../containers/ItemReferencesContainer'
import ItemSelectContainer from '../containers/ItemSelectContainer'
import ModelSelectContainer from '../containers/ModelSelectContainer'
import FormRow from './form/FormRow.jsx'

const ADD = 'ADD'

const SaveButton = ({readonly, save, saving}) => (
    <button
        type="button"
        className="btn btn-primary"
        onClick={save}
        disabled={saving || readonly}>
        Save
    </button>
)

const SimulateButton = ({readonly, simulate, simulating}) => (
    <button
        type="button"
        className="btn btn-default"
        onClick={simulate}
        disabled={simulating || readonly}>
        <span>Run simulations</span>
        {simulating && <div className="loader" />}
    </button>
)

const Content = ({model}) => (
    <div>
        <ItemReferencesContainer />
        <Selects model={model} />
        <ItemFormContainer />
    </div>
)

const Selects = ({model}) => (
    <div className="form-horizontal">
        <FormRow label="Select model">
            <ModelSelectContainer />
        </FormRow>
        { model && (
            <FormRow label="Select item">
                <Row>
                    <Col sm={6}><ItemSelectContainer /></Col>
                    <Col sm={6}><ItemAddContainer /></Col>
                </Row>
            </FormRow>
        )}
    </div>
)

const Main = ({labels, model, tab, tabs, addTab, changeTab, deleteTab, ...props}) => {
    const onSelect = value => {
        if (value === ADD) {
            addTab()
        } else {
            changeTab(value)
        }
    }

    const tabItems = (
        <Nav bsStyle="tabs" style={{paddingRight: 100}}>
            {tabs.map(t => (
                <NavItem key={t} eventKey={t}>
                    <div style={{display: 'inline-block', minWidth: 100}}>
                        {labels.get(t)}
                    </div>
                    <button
                        type="button"
                        style={{marginLeft: 15, marginRight: -10}}
                        className="btn btn-xs btn-link"
                        disabled={tabs.size === 1}
                        onClick={() => deleteTab(t)}>
                        &#10060;
                    </button>
                </NavItem>
            ))}
            <NavItem eventKey={ADD}>&#10133;</NavItem>
        </Nav>
    )

    return (
        <Tab.Container id="main-tabs" activeKey={tab} onSelect={onSelect}>
            <div>
                <div className="btn-toolbar pull-right">
                    <SimulateButton {...props} />
                    <SaveButton {...props} />
                </div>
                {tabItems}
                <Tab.Content style={{marginTop: '1em'}}>
                    <Content model={model} />
                </Tab.Content>
            </div>
        </Tab.Container>
    )
}

export default Main
