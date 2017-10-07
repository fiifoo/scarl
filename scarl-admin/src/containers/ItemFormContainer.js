import React from 'react'
import { connect } from 'react-redux'
import { setItemValue, showSideForm, hideSideForm } from '../actions/actions'
import ItemForm from '../components/ItemForm.jsx'

const ItemFormIf = props => props.item ? <ItemForm {...props} /> : <div />

const ModelContainer = connect(
    state => {
        const model = state.ui.model && state.models.main.get(state.ui.model)
        const item = model && state.ui.item && state.data.getIn(model.dataPath.concat([state.ui.item]))

        return {
            item,
            model,
            sideForm: state.ui.sideForm,
            data: state.data,
            models: state.models,
        }
    }, {
        setItemValue,
        showSideForm,
        hideSideForm,
    }
)(ItemFormIf)

export default ModelContainer
