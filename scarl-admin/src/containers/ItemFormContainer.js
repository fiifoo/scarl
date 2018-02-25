import React from 'react'
import { connect } from 'react-redux'
import { renameItem, deleteItem, setItemRenameId, setItemValue, showItemReferences, showSideForm, hideSideForm } from '../actions/actions'
import ItemForm from '../components/ItemForm.jsx'
import { tabState } from '../reducers/ui/utils'

const ItemFormIf = props => props.item ? <ItemForm {...props} /> : <div />

const ItemFormContainer = connect(
    state => {
        const formState = tabState(state.ui.form)

        const model = formState.model && state.models.main.get(formState.model)
        const item = model && formState.item && state.data.getIn(model.dataPath.concat([formState.item]))

        return {
            item,
            itemRename: formState.itemRename,
            model,
            sideForm: formState.sideForm,
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
