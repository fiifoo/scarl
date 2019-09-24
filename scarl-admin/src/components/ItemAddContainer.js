import React from 'react'
import { connect } from 'react-redux'
import { addItem, setItemAddId } from '../actions/actions'
import { tabState } from '../reducers/ui/utils'
import ItemAdd from './ItemAdd.jsx'

const ItemAddIf = props => props.model ? <ItemAdd {...props} /> : <div />

const ItemAddContainer = connect(
    state => {
        const formState = tabState(state.ui.form)
        const model = formState.model && state.models.main.get(formState.model)

        return {
            model,
            id: formState.itemAdd.id,
            invalid: formState.itemAdd.invalid,
            copyId: formState.item,
        }
    }, {
        addItem,
        setId: setItemAddId,
    }
)(ItemAddIf)

export default ItemAddContainer
