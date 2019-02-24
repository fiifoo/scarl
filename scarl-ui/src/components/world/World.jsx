import { List } from 'immutable'
import React from 'react'
import System from './System.jsx'
import { getControlledTransport } from '../../game/world'
import { TICK } from '../../system/SolarSystem'

import './World.css'

const humanizeDuration = (seconds, asText = true) => {
    const totalHours = Math.floor(seconds / 3600)
    const hours = totalHours % 24
    const days = Math.floor(totalHours / 24)

    const duration = {
        days: days === 0 ? null : (
            days === 1 ? '1 day' : days + ' days'
        ),
        hours: hours === 1 ? '1 hour' : hours + ' hours'
    }

    return ! asText ? duration : (
        duration.days ? duration.days + ' ' + duration.hours : duration.hours
    )
}

const TravelInfo = ({travel}) => {
    if (! travel.possible) {
        return <b className="text-danger">Unreachable</b>
    }

    const eta = travel.travel && travel.travel.eta || TICK

    const duration = humanizeDuration(eta, false)

    return (
        <div>
            <div><b>ETA: </b></div>
            {duration.days && <div><b>{duration.days}</b></div>}
            <div><b>{duration.hours}</b></div>
        </div>
    )
}

const Region = ({ui, world, region, actions}) => {
    const transports = world.transportRegions.filter(x => x === region.id).map((_, x) => x).toSet()

    const controlledTransport = getControlledTransport(world)
    const isControlledTransportRegion = controlledTransport && transports.contains(controlledTransport.id)

    const traveling = ui.travel && ui.travel.simulate

    const canTravel = controlledTransport && ! isControlledTransportRegion
    const canDisembark = isControlledTransportRegion ? region.entrances.get(controlledTransport.category, List()) : List()
    const canEmbark = transports.filter(transport => {
        const category = world.transports.get(transport).category

        return region.exits.get(category, List()).contains(world.site)
    })

    const active = isControlledTransportRegion || world.siteRegions.get(world.site) === region.id

    return (
        <div className={active ? 'region active' : 'region'}>
            <div className="region-header clearfix">

                {! traveling && (
                    <div className="actions pull-right">
                        {canTravel && (
                            ui.travel && ui.travel.to === region.id ? (
                                <div className="text-right">
                                    <button
                                        type="button"
                                        className="btn btn-sm btn-primary"
                                        onClick={() => actions.travel()}
                                        disabled={!ui.travel.possible}>
                                        Travel
                                    </button>
                                    <div className="text-center">
                                        <TravelInfo travel={ui.travel} />
                                    </div>
                                </div>
                            ) : (
                                <button
                                    type="button"
                                    className="btn btn-sm btn-default"
                                    onClick={() => actions.calculateTravel(region.id)}>
                                    Travel
                                </button>
                            )
                        )}
                    </div>
                )}

                <div className="title">{region.id}</div>
            </div>

            {transports.map((transport) => (
                <div key={transport} className="region-transport">

                    {! traveling && (
                        <div className="actions pull-right">
                            {canEmbark.contains(transport) && (
                                <button
                                    type="button"
                                    className="btn btn-sm btn-primary"
                                    onClick={() => actions.embark(transport)}>
                                    Embark
                                </button>
                            )}
                        </div>
                    )}

                    <div className="title">{transport}</div>
                </div>
            ))}

            {canDisembark.map(to => (
                <div key={to} className="region-site">

                    {! traveling && (
                        <div className="actions pull-right">
                            <button
                                type="button"
                                className="btn btn-sm btn-primary"
                                onClick={() => actions.disembark(to)}>
                                Disembark
                            </button>
                        </div>
                    )}

                    <div className="title">{to}</div>
                </div>
            ))}
        </div>
    )
}

const World = ({spaceships, stellarBodies, ui, world, ...actions}) =>  (
    <div>
        <div style={{display: 'table-cell', verticalAlign: 'top'}}>
            <h4 className="text-right">
                Time passed: {humanizeDuration(world.system.time)}
            </h4>

            {world.regions.filter(x => ! x.entrances.isEmpty()).map(region => (
                <Region key={region.id} ui={ui} world={world} region={region} actions={actions} />
            )).toArray()}
        </div>
        <div style={{display: 'table-cell', verticalAlign: 'top'}}>
            <System
                spaceships={spaceships}
                stellarBodies={stellarBodies}
                ui={ui}
                world={world}
                clearTravel={actions.clearTravel} />
        </div>
    </div>
)

export default World
