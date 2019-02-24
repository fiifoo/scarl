import { connect } from 'react-redux'
import * as worldActions from '../../actions/worldActions'
import { WORLD } from '../../game/modes'
import GameView from '../game/GameView.jsx'
import World from './World.jsx'

const WorldContainer = connect(
    state => ({
        component: World,
        visible: state.ui.game.mode === WORLD,

        spaceships: state.spaceships,
        stellarBodies: state.stellarBodies,
        ui: state.ui.world,
        world: state.world,
    }), {
        ...worldActions,
    }
)(GameView)

export default WorldContainer
