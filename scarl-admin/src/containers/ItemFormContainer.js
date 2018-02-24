import React from 'react'
import { connect } from 'react-redux'
import { renameItem, deleteItem, setItemRenameId, setItemValue, showItemReferences, showSideForm, hideSideForm } from '../actions/actions'
import ItemForm from '../components/ItemForm.jsx'

const ItemFormIf = props => props.item ? <ItemForm {...props} /> : <div />

const ItemFormContainer = connect(
    state => {
        const model = state.ui.main.model && state.models.main.get(state.ui.main.model)
        const item = model && state.ui.main.item && state.data.getIn(model.dataPath.concat([state.ui.main.item]))

        return {
            item,
            itemRename: state.ui.main.itemRename,
            model,
            sideForm: state.ui.main.sideForm,
            data: state.data,
            models: state.models,
        }
    }, {
        deleteItem,
        renameItem,
        setItemRenameId,
        setItemValue,
        showItemReferences,
        showSideForm,
        hideSideForm,
    }
)(ItemFormIf)

export default ItemFormContainer
