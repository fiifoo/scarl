import React from 'react'
import { Nav, Navbar, NavItem } from 'react-bootstrap'
import * as pages from '../const/pages'
import MainContainer from './MainContainer'
import ColumnEditorContainer from './columnEditor/ColumnEditorContainer'
import SummaryContainer from './summary/SummaryContainer'

import 'react-select/dist/react-select.css'
import './App.css'

const SaveButton = ({readonly, save, saving, unsaved}) => (
    <button
        type="button"
        className={unsaved ? 'btn btn-primary navbar-btn' : 'btn btn-default navbar-btn'}
        onClick={save}
        disabled={saving || readonly}>
        Save
    </button>
)

const SimulateButton = ({readonly, simulate, simulating}) => (
    <button
        type="button"
        className="btn btn-default navbar-btn"
        onClick={simulate}
        disabled={simulating || readonly}>
        <span>Run simulations</span>
        {simulating && <div className="loader" />}
    </button>
)


const Navigation = ({page, changePage, fetchingSummary, ...props}) => (
    <Navbar fluid={true}>
        <Navbar.Header>
            <Navbar.Brand>
                Scarl - Admin
            </Navbar.Brand>
        </Navbar.Header>
        <Nav bsStyle="pills" activeKey={page} onSelect={changePage}>
            <NavItem eventKey={pages.MAIN}>Main</NavItem>
            <NavItem eventKey={pages.COLUMN_EDITOR}>Column Editor</NavItem>
            <NavItem eventKey={pages.SUMMARY}>
                <span>Summary</span>
                {fetchingSummary && <div className="loader" />}
            </NavItem>
        </Nav>
        <div className="btn-toolbar pull-right">
            <SimulateButton {...props} />
            <SaveButton {...props} />
        </div>
    </Navbar>
)

const Page = ({page}) => {
    switch (page) {
        case pages.SUMMARY: {
            return <SummaryContainer />
        }
        case pages.COLUMN_EDITOR: {
            return <ColumnEditorContainer />
        }
        case pages.MAIN:
        default: {
            return <MainContainer />
        }
    }
}

const App = props => {
    const {page} = props

    return (
        <div>
            <Navigation {...props} />
            <div className="container-fluid">
                <Page page={page} />
            </div>
        </div>
    )
}

export default App
