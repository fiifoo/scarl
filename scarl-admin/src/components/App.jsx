import React from 'react'
import { Col, Row } from 'react-bootstrap'
import ItemAddContainer from '../containers/ItemAddContainer'
import ItemFormContainer from '../containers/ItemFormContainer'
import ItemSelectContainer from '../containers/ItemSelectContainer'
import ModelSelectContainer from '../containers/ModelSelectContainer'
import FormRow from './form/FormRow.jsx'

import 'react-select/dist/react-select.css'
import './App.css'

const Nav = () => (
    <nav className="navbar navbar-default">
        <div className="container-fluid">
            <div className="navbar-header">
                <div className="navbar-brand">Scarl - Admin</div>
            </div>
        </div>
    </nav>
)

const Controls = ({model, ...props}) => (
    <Row>
        <Col md={6}>
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
        </Col>
    </Row>
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

const App = props => (
    <div>
        <Nav />
        <div className="container-fluid">
            <Controls {...props} />
            <hr />
            <ItemFormContainer />
        </div>
    </div>
)

export default App
