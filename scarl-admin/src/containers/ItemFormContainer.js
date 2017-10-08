import React from 'react'
import { connect } from 'react-redux'
import { setItemValue, showSideForm, hideSideForm } from '../actions/actions'
import ItemForm from '../components/ItemForm.jsx'

const ItemFormIf = props => props.item ? <ItemForm {...props} /> : <div />

const ModelContainer = connect(
    state => {
        const model = state.ui.main.model && state.models.main.get(state.ui.main.model)
        const item = model && state.ui.main.item && state.data.getIn(model.dataPath.concat([state.ui.main.item]))

        return {
            item,
            model,
            sideForm: state.ui.main.sideForm,
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
