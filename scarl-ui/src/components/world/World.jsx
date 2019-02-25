import React from 'react'
import Region from './Region.jsx'
import System from './System.jsx'
import { humanizeDuration } from './utils'

import './World.css'

const LEFT_COLUMN_WIDTH = 400
const MIN_RIGHT_COLUMN_WIDTH = 200

const World = ({spaceships, stellarBodies, ui, viewSize, world, ...actions}) => {
    const leftStyle = {
        display: 'inline-block',
        verticalAlign: 'top',
        width: LEFT_COLUMN_WIDTH,
    }
    const rightStyle = {
        display: 'inline-block',
        verticalAlign: 'top',
        marginLeft: 20,
        overflow: 'hidden',
        height: viewSize.height,
        width: Math.max(viewSize.width - LEFT_COLUMN_WIDTH, MIN_RIGHT_COLUMN_WIDTH) - 20,
    }

    const systemViewSize = {
        width: rightStyle.width - rightStyle.marginLeft,
        height: viewSize.height,
    }

    return (
        <div style={{whiteSpace: 'nowrap'}}>
            <div style={leftStyle}>
                <h4 className="text-right">
                    Time passed: {humanizeDuration(world.system.time)}
                </h4>

                {world.regions.filter(x => ! x.entrances.isEmpty()).map(region => (
                    <Region key={region.id} ui={ui} world={world} region={region} actions={actions} />
                )).toArray()}
            </div>
            <div style={rightStyle}>
                <System
                    spaceships={spaceships}
                    stellarBodies={stellarBodies}
                    ui={ui}
                    viewSize={systemViewSize}
                    world={world}
                    actions={actions} />
            </div>
        </div>
    )
}

export default World
