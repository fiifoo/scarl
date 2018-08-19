import React from 'react'
import { connect } from 'react-redux'
import { renameItem, deleteItem, setItemRenameId, setItemValue, addTag, showItem, showItemReferences, showSideForm, hideSideForm } from '../actions/actions'
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
            tags: state.tags,
        }
    }, {
        deleteItem,
        renameItem,
        setItemRenameId,
        setItemValue,
        addTag,
        showItem,
        showItemReferences,
        showSideForm,
        hideSideForm,
    }
)(ItemFormIf)

export default ItemFormContainer
