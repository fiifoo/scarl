import { Map } from 'immutable'
import { connect } from 'react-redux'
import { addTab, changeTab, deleteTab, save, simulate } from '../actions/actions'
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
        simulating: state.ui.main.simulating,
        tab: state.ui.main.tab,
        tabs: state.ui.main.tabs,
        unsaved: state.hashCode !== state.data.hashCode()
    }), {
        addTab, changeTab, deleteTab,
        save,
        simulate,
    }
)(Main)

export default MainContainer
