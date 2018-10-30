import React from 'react'
import { connect } from 'react-redux'
import { setLookDetailed } from '../../actions/lookActions'
import { LOOK } from '../../game/modes.js'
import LookDescription from './LookDescription.jsx'

const ComponentIf = ({mode, ...props}) => mode === LOOK && props.location ? (
    <LookDescription {...props} />
): (
    <div />
)

const LookDescriptionContainer = connect(
    state => ({
        mode: state.ui.game.mode,
        location: state.ui.game.cursor,
        detailed: state.ui.look.detailed,

        factions: state.factions,
        fov: state.fov.cumulative,
        map: state.area.map,
        kinds: state.kinds,
        player: state.player,
    }), {
        setLookDetailed,
    }
)(ComponentIf)

export default LookDescriptionContainer
