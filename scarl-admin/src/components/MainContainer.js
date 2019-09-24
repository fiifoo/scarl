import { connect } from 'react-redux'
import { addTabSet, deleteTabSet, renameTabSet, saveUi, sortTabSets, toggleTabSet } from '../actions/actions'
import { addTab, changeTab, deleteTab, sortTabs } from '../actions/actions'
import { shouldSaveUi } from '../data/ui/utils'
import { tabState } from '../reducers/ui/utils'
import Main from './Main.jsx'

const MainContainer = connect(
    state => ({
        model: tabState(state.ui.form).model,
        readonly: state.readonly,
        shouldSaveUi: shouldSaveUi(state),
        tab: state.ui.main.tab,
        tabContents: state.ui.form.tabs,
        tabs: state.ui.main.tabs,
        tabSets: state.ui.main.tabSets,
    }), {
        addTabSet,
        deleteTabSet,
        renameTabSet,
        saveUi,
        sortTabSets,
        toggleTabSet,

        addTab,
        changeTab,
        deleteTab,
        sortTabs,
    }
)(Main)

export default MainContainer
