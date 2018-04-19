import { connect } from 'react-redux'
import { inventory, keyBindings, messageLog } from '../../actions/gameActions'
import ActionBar from './ActionBar.jsx'

const ActionBarContainer = connect(
    () => ({}), {
        inventory,
        keyBindings,
        messageLog,
    }
)(ActionBar)

export default ActionBarContainer
