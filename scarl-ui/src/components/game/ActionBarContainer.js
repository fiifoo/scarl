import { connect } from 'react-redux'
import { crafting, inventory, keyBindings, messageLog, playerInfo, world } from '../../actions/gameActions'
import ActionBar from './ActionBar.jsx'

const ActionBarContainer = connect(
    () => ({}), {
        crafting,
        inventory,
        keyBindings,
        messageLog,
        playerInfo,
        world,
    }
)(ActionBar)

export default ActionBarContainer
