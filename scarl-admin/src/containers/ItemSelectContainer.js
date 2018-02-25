import React from 'react'
import { connect } from 'react-redux'
import { selectItem } from '../actions/actions'
import ItemSelect from '../components/ItemSelect.jsx'
import { tabState } from '../reducers/ui/utils'

const ItemSelectIf = ({model, ...props}) => model ? <ItemSelect {...props} /> : <div />

const ItemSelectContainer = connect(
    state => {
        const formState = tabState(state.ui.form)

        const model = formState.model && state.models.main.get(formState.model)
        const items = model && state.data.getIn(model.dataPath)

        return {
            item: formState.item,
            items,
            model,
        }
    }, {
        selectItem,
    }
)(ItemSelectIf)

export default ItemSelectContainer
