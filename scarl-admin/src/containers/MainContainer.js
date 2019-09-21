import { connect } from 'react-redux'
import { addTab, addTabSet, changeTab, deleteTab, deleteTabSet, renameTabSet, sortTabs, sortTabSets, toggleTabSet } from '../actions/actions'
import Main from '../components/Main.jsx'
import { tabState } from '../reducers/ui/utils'

const MainContainer = connect(
    state => ({
        model: tabState(state.ui.form).model,
        tab: state.ui.main.tab,
        tabContents: state.ui.form.tabs,
        tabs: state.ui.main.tabs,
        tabSets: state.ui.main.tabSets,
    }), {
        addTab,
        addTabSet,
        changeTab,
        deleteTab,
        deleteTabSet,
        renameTabSet,
        sortTabs,
        sortTabSets,
        toggleTabSet,
    }
)(Main)

export default MainContainer
