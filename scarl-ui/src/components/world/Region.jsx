import { List } from 'immutable'
import React from 'react'
import { getControlledTransport } from '../../game/world'
import { TICK } from '../../system/SolarSystem'
import { humanizeDuration } from './utils'

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

    const active = isControlledTransportRegion || world.sites.get(world.site).region === region.id

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

                <div className="title">{region.name}</div>
            </div>

            {active && (
                <div className="title active">{world.sites.get(world.site).name}</div>
            )}

            {transports.map((transport) => (
                <div key={transport}>

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

                    <div className="title">{world.transports.get(transport).name}</div>
                </div>
            ))}

            {canDisembark.map(to => (
                <div key={to}>

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

                    <div className="title">{world.sites.get(to).name || to}</div>
                </div>
            ))}
        </div>
    )
}

export default Region
