import React from 'react'
import { Nav, Navbar, NavItem } from 'react-bootstrap'
import * as pages from '../const/pages'
import ColumnEditorContainer from '../containers/ColumnEditorContainer'
import MainContainer from '../containers/MainContainer'
import SummaryContainer from '../containers/SummaryContainer'

import 'react-select/dist/react-select.css'
import './App.css'

const Navigation = ({page, changePage, fetchingSummary}) => (
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

const App = ({page, changePage, fetchingSummary}) => (
    <div>
        <Navigation page={page} changePage={changePage} fetchingSummary={fetchingSummary} />
        <div className="container-fluid">
            <Page page={page} />
        </div>
    </div>
)

export default App
