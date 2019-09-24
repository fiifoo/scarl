import { connect } from 'react-redux'
import { hideItemReferences, showReferenceItem } from '../actions/actions'
import { tabState } from '../reducers/ui/utils'
import ItemReferences from './ItemReferences.jsx'

const ItemReferencesContainer = connect(
    state => ({
        references: tabState(state.ui.form).itemReferences,
    }), {
        hideItemReferences,
        showReferenceItem,
    }
)(ItemReferences)

export default ItemReferencesContainer
