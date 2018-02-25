import { connect } from 'react-redux'
import { hideItemReferences, showReferenceItem } from '../actions/actions'
import ItemReferences from '../components/ItemReferences.jsx'
import { tabState } from '../reducers/ui/utils'

const ItemReferencesContainer = connect(
    state => ({
        references: tabState(state.ui.form).itemReferences,
    }), {
        hideItemReferences,
        showReferenceItem,
    }
)(ItemReferences)

export default ItemReferencesContainer
