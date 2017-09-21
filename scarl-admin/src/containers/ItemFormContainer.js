import React from 'react'
import { connect } from 'react-redux'
import { setItemValue } from '../actions/actions'
import ItemForm from '../components/ItemForm.jsx'

const ItemFormIf = props => props.item ? <ItemForm {...props} /> : <div />

const ModelContainer = connect(
    state => {
        const model = state.ui.model && state.models.main.get(state.ui.model)
        const item = model && state.ui.item && state.data.getIn(model.dataPath.concat([state.ui.item]))

        return {
            item,
            model,
            data: state.data,
            models: state.models,
        }
    }, {
        setItemValue,
    }
)(ItemFormIf)

export default ModelContainer
