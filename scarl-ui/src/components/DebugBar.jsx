import React from 'react'
import * as modes from '../debug/modes'

const className = 'btn btn-default'
const activeClassName = 'btn btn-warning'
const getClassName = active => active ? activeClassName : className

const FovButton = ({mode, cancelMode, debugFov}) => {
    const active = mode === modes.FOV
    const onClick = active ? cancelMode : debugFov

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            >Debug FOV</button>
    )
}

const WaypointButton = ({mode, cancelMode, debugWaypoint}) => {
    const active = mode === modes.WAYPOINT
    const onClick = active ? cancelMode : debugWaypoint

    return (
        <button
            className={getClassName(active)}
            onClick={onClick}
            >Debug waypoints</button>
    )
}

const DebugBar = props =>  {
    const {mode, cancelMode} = props
    const {debugFov, debugWaypoint} = props

    return (
        <div className="btn-toolbar">
            <FovButton
                mode={mode}
                cancelMode={cancelMode}
                debugFov={debugFov}
                />
            <WaypointButton
                mode={mode}
                cancelMode={cancelMode}
                debugWaypoint={debugWaypoint}
                />
        </div>
    )
}

export default DebugBar
