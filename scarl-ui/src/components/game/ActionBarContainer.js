import { connect } from 'react-redux'
import { crafting, inventory, keyBindings, messageLog, playerInfo } from '../../actions/gameActions'
import ActionBar from './ActionBar.jsx'

const ActionBarContainer = connect(
    () => ({}), {
        crafting,
        inventory,
        keyBindings,
        messageLog,
        playerInfo,
    }
)(ActionBar)

export default ActionBarContainer
