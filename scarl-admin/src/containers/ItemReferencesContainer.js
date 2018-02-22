import React from 'react'
import { connect } from 'react-redux'
import { hideItemReferences } from '../actions/actions'
import ItemReferences from '../components/ItemReferences.jsx'

const ItemReferencesContainer = connect(
    state => ({
        references: state.ui.main.itemReferences,
    }), {
        hideItemReferences,
    }
)(ItemReferences)

export default ItemReferencesContainer
