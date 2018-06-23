import React from 'react'
import MenuItem from '../MenuItem.jsx'
import * as modes from '../../debug/modes'

const FovButton = ({mode, cancelMode, debugFov}) => {
    const active = mode === modes.FOV
    const onClick = active ? cancelMode : debugFov

    return <MenuItem onClick={onClick} label="Debug FOV" />
}

const WaypointButton = ({mode, cancelMode, debugWaypoint}) => {
    const active = mode === modes.WAYPOINT
    const onClick = active ? cancelMode : debugWaypoint

    return <MenuItem onClick={onClick} label="Debug waypoints" />
}

const DebugBar = props =>  {
    const {mode, cancelMode} = props
    const {debugFov, debugWaypoint} = props

    return (
        <div>
            <FovButton
                mode={mode}
                cancelMode={cancelMode}
                debugFov={debugFov} />
            <WaypointButton
                mode={mode}
                cancelMode={cancelMode}
                debugWaypoint={debugWaypoint} />
        </div>
    )
}

export default DebugBar
