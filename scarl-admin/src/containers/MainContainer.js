import { Map } from 'immutable'
import { connect } from 'react-redux'
import { addTab, changeTab, deleteTab, save } from '../actions/actions'
import Main from '../components/Main.jsx'
import { tabState } from '../reducers/ui/utils'

const getLabels = state => Map(state.ui.main.tabs.map(tab => (
    [tab, state.ui.form.tabs.getIn([tab, 'item']) || `Tab ${tab}`]
)))

const MainContainer = connect(
    state => ({
        labels: getLabels(state),
        model: tabState(state.ui.form).model,
        readonly: state.readonly,
        saving: state.ui.main.saving,
        tab: state.ui.main.tab,
        tabs: state.ui.main.tabs,
    }), {
        addTab, changeTab, deleteTab,
        save,
    }
)(Main)

export default MainContainer
