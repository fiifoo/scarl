import React from 'react'
import { Col, Nav, Navbar, NavItem, Row, } from 'react-bootstrap'
import * as pages from '../const/pages'
import ItemAddContainer from '../containers/ItemAddContainer'
import ItemFormContainer from '../containers/ItemFormContainer'
import ItemReferencesContainer from '../containers/ItemReferencesContainer'
import ItemSelectContainer from '../containers/ItemSelectContainer'
import ModelSelectContainer from '../containers/ModelSelectContainer'
import SummaryContainer from '../containers/SummaryContainer'
import FormRow from './form/FormRow.jsx'

import 'react-select/dist/react-select.css'
import './App.css'

const Navigation = ({page, changePage}) => (
    <Navbar>
        <Navbar.Header>
            <Navbar.Brand>
                Scarl - Admin
            </Navbar.Brand>
        </Navbar.Header>
        <Nav bsStyle="pills" activeKey={page} onSelect={changePage}>
            <NavItem eventKey={pages.MAIN}>Main</NavItem>
            <NavItem eventKey={pages.SUMMARY}>Summary</NavItem>
        </Nav>
    </Navbar>
)

const Controls = ({model, ...props}) => (
    <div className="form-horizontal">
        <FormRow label="">
            <SaveButton {...props} />
        </FormRow>
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

const SaveButton = ({readonly, save, saving}) => (
    <button
        type="button"
        className="btn btn-primary"
        onClick={save}
        disabled={saving || readonly}>
        Save
    </button>
)

const App = ({page, changePage, ...props}) => (
    <div>
        <Navigation page={page} changePage={changePage} />
        <ItemReferencesContainer />
        <div className="container-fluid">
            {page === pages.SUMMARY ? (
                <SummaryContainer />
            ) : (
                <div>
                    <Controls {...props} />
                    <hr />
                    <ItemFormContainer />
                </div>
            )}
        </div>
    </div>
)

export default App
