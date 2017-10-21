import React from 'react'
import { connect } from 'react-redux'
import { selectSummaryCreature } from '../actions/actions'
import Summary from '../components/Summary.jsx'

const SummaryIf = props => props.summary ? (
    <Summary {...props} />
) : (
    <div />
)

const SummaryContainer = connect(
    state => ({
        creature: state.ui.summary.creature,
        summary: state.summary,
    }), {
        selectCreature: selectSummaryCreature,
    }
)(SummaryIf)

export default SummaryContainer
