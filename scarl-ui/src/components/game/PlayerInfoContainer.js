import { connect } from 'react-redux'
import { PLAYER_INFO } from '../../game/modes'
import GameView from './GameView.jsx'
import PlayerInfo from './PlayerInfo.jsx'

const PlayerInfoContainer = connect(
    state => ({
        component: PlayerInfo,
        visible: state.ui.game.mode === PLAYER_INFO,

        equipments: state.equipments,
        inventory: state.inventory,
        kinds: state.kinds,
        player: state.player,
    })
)(GameView)

export default PlayerInfoContainer
