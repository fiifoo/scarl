import React from 'react'
import { connect } from 'react-redux'
import { selectItem } from '../actions/actions'
import ItemSelect from '../components/ItemSelect.jsx'

const ItemSelectIf = ({model, ...props}) => model ? <ItemSelect {...props} /> : <div />

const ItemSelectContainer = connect(
    state => {
        const model = state.ui.main.model && state.models.main.get(state.ui.main.model)
        const items = model && state.data.getIn(model.dataPath)

        return {
            item: state.ui.main.item,
            items,
            model,
        }
    }, {
        selectItem,
    }
)(ItemSelectIf)

export default ItemSelectContainer
