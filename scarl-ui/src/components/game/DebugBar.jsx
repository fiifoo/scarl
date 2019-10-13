import React from 'react'
import MenuItem from '../MenuItem.jsx'
import * as modes from '../../debug/modes'

const FovButton = ({mode, cancelMode, debugFov}) => {
    const active = mode === modes.FOV
    const onClick = active ? cancelMode : debugFov

    return <MenuItem onClick={onClick} label="Debug FOV" />
}

const PartyButton = ({mode, cancelMode, debugParty}) => {
    const active = mode === modes.PARTY
    const onClick = active ? cancelMode : debugParty

    return <MenuItem onClick={onClick} label="Debug parties" />
}

const WaypointButton = ({mode, cancelMode, debugWaypoint}) => {
    const active = mode === modes.WAYPOINT
    const onClick = active ? cancelMode : debugWaypoint

    return <MenuItem onClick={onClick} label="Debug waypoints" />
}

const DebugBar = props =>  {
    const {mode, cancelMode} = props
    const {debugFov, debugParty, debugWaypoint} = props

    return (
        <div>
            <FovButton
                mode={mode}
                cancelMode={cancelMode}
                debugFov={debugFov} />
            <PartyButton
                mode={mode}
                cancelMode={cancelMode}
                debugParty={debugParty} />
            <WaypointButton
                mode={mode}
                cancelMode={cancelMode}
                debugWaypoint={debugWaypoint} />
        </div>
    )
}

export default DebugBar
