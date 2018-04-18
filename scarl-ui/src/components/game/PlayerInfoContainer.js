import { connect } from 'react-redux'
import PlayerInfo from './PlayerInfo.jsx'

const PlayerInfoContainer = connect(
    state => ({
        player: state.player,
    })
)(PlayerInfo)

export default PlayerInfoContainer
