import React from 'react'
import { connect } from 'react-redux'
import { deleteItem, renameItem, setItemRenameId } from '../actions/actions'
import { addTag, setItemValue  } from '../actions/actions'
import { hideSideForm, showItem, showItemReferences, showSideForm } from '../actions/actions'
import { addAdjacentTab } from '../actions/actions'
import { tabState } from '../reducers/ui/utils'
import ItemForm from './ItemForm.jsx'

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

        addTag,
        setItemValue,

        hideSideForm,
        showItem,
        showItemReferences,
        showSideForm,

        addTab: addAdjacentTab
    }
)(ItemFormIf)

export default ItemFormContainer
