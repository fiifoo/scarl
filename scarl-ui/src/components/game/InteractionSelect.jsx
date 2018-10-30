import React from 'react'
import { getInteractionDescription } from '../../game/interaction.js'

const InteractionSelect = ({selected, interactions, kinds, selectInteraction}) => {

    const getDescription = interaction => (
        `Interact: ${getInteractionDescription(kinds, interaction)}`
    )

    const renderInteraction = (interaction, key) => (
        <div
            key={key}
            className={key === selected ? 'active' : null}
            onClick={() => selectInteraction(key)}>
            {getDescription(interaction)}
        </div>
    )

    return (
        <div className="screen-dropdown">
            {interactions.map(renderInteraction)}
        </div>
    )
}

export default InteractionSelect
