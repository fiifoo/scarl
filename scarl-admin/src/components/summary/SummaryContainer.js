import React from 'react'
import { connect } from 'react-redux'
import { changeSummaryTab, selectSummaryCreature } from '../../actions/actions'
import Summary from './Summary.jsx'

const SummaryIf = props => props.summary ? (
    <Summary {...props} />
) : (
    <div />
)

const SummaryContainer = connect(
    state => ({
        creature: state.ui.summary.creature,
        summary: state.summary,
        tab: state.ui.summary.tab,
    }), {
        changeTab: changeSummaryTab,
        selectCreature: selectSummaryCreature,
    }
)(SummaryIf)

export default SummaryContainer
