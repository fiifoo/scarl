import React from 'react'
import { Nav, Navbar, NavItem } from 'react-bootstrap'
import * as pages from '../const/pages'
import MainContainer from '../containers/MainContainer'
import SummaryContainer from '../containers/SummaryContainer'

import 'react-select/dist/react-select.css'
import './App.css'

const Navigation = ({page, changePage, fetchingSummary}) => (
    <Navbar>
        <Navbar.Header>
            <Navbar.Brand>
                Scarl - Admin
            </Navbar.Brand>
        </Navbar.Header>
        <Nav bsStyle="pills" activeKey={page} onSelect={changePage}>
            <NavItem eventKey={pages.MAIN}>Main</NavItem>
            <NavItem eventKey={pages.SUMMARY}>
                <span>Summary</span>
                {fetchingSummary && <div className="loader" />}
            </NavItem>
        </Nav>
    </Navbar>
)
const App = ({page, changePage, fetchingSummary}) => (
    <div>
        <Navigation page={page} changePage={changePage} fetchingSummary={fetchingSummary} />
        <div className="container-fluid">
            {page === pages.SUMMARY ? (
                <SummaryContainer />
            ) : (
                <MainContainer />
            )}
        </div>
    </div>
)

export default App
