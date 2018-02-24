import React from 'react'
import { connect } from 'react-redux'
import { addItem, setItemAddId } from '../actions/actions'
import ItemAdd from '../components/ItemAdd.jsx'

const ItemAddIf = props => props.model ? <ItemAdd {...props} /> : <div />

const ItemAddContainer = connect(
    state => {
        const model = state.ui.main.model && state.models.main.get(state.ui.main.model)

        return {
            model,
            id: state.ui.main.itemAdd.id,
            invalid: state.ui.main.itemAdd.invalid,
        }
    }, {
        addItem,
        setId: setItemAddId,
    }
)(ItemAddIf)

export default ItemAddContainer
