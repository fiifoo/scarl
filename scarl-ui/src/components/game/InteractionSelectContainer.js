import React from 'react'
import { connect } from 'react-redux'
import { INTERACT } from '../../game/modes.js'
import { selectInteraction } from '../../actions/gameActions'
import InteractionSelect from './InteractionSelect.jsx'

const ComponentIf = ({mode, ...props}) => mode === INTERACT ? (
    <InteractionSelect {...props} />
): (
    <div />
)

const InteractionSelectContainer = connect(
    state => ({
        mode: state.ui.game.mode,
        selected: state.ui.game.interaction,
        interactions: state.ui.game.interactions,
        kinds: state.kinds,
    }), {
        selectInteraction,
    }
)(ComponentIf)

export default InteractionSelectContainer
