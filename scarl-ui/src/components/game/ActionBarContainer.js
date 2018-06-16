import { connect } from 'react-redux'
import { inventory, keyBindings, messageLog, playerInfo } from '../../actions/gameActions'
import ActionBar from './ActionBar.jsx'

const ActionBarContainer = connect(
    () => ({}), {
        inventory,
        keyBindings,
        messageLog,
        playerInfo,
    }
)(ActionBar)

export default ActionBarContainer
