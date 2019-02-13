import { List } from 'immutable'
import React from 'react'
import SystemContainer from './SystemContainer'

import './World.css'

const Region = ({ui, world, region, actions}) => {
    const transports = world.transportRegions.filter(x => x === region.id).map((_, x) => x).toSet()

    const controlledTransport = world.transports.find(x => x.hub === world.site)
    const isControlledTransportRegion = controlledTransport && transports.contains(controlledTransport.id)

    const traveling = !!ui.travelSimulation

    const canTravel = controlledTransport && ! isControlledTransportRegion
    const canDisembark = isControlledTransportRegion ? region.entrances.get(controlledTransport.category, List()) : List()
    const canEmbark = transports.filter(transport => {
        const category = world.transports.get(transport).category

        return region.exits.get(category, List()).contains(world.site)
    })

    const active = isControlledTransportRegion || world.siteRegions.get(world.site) === region.id

    return (
        <div className={active ? 'region active' : 'region'}>
            <div className="region-header">

                {! traveling && (
                    <div className="actions pull-right btn-toolbar">
                        {canTravel && (
                            <button
                                type="button"
                                className="btn btn-sm btn-default"
                                onClick={() => actions.travel(region.id)}>
                                Travel
                            </button>
                        )}
                    </div>
                )}

                <div className="title">{region.id}</div>
            </div>

            {transports.map((transport) => (
                <div key={transport} className="region-transport">

                    {! traveling && (
                        <div className="actions pull-right btn-toolbar">
                            {canEmbark.contains(transport) && (
                                <button
                                    type="button"
                                    className="btn btn-sm btn-default"
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
                        <div className="actions pull-right btn-toolbar">
                            <button
                                type="button"
                                className="btn btn-sm btn-default"
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

const World = ({ui, world, ...actions}) =>  (
    <div>
        <div style={{display: 'table-cell', verticalAlign: 'top'}}>
            {world.regions.filter(x => ! x.entrances.isEmpty()).map(region => (
                <Region key={region.id} ui={ui} world={world} region={region} actions={actions} />
            )).toArray()}
        </div>
        <div style={{display: 'table-cell', verticalAlign: 'top'}}>
            <SystemContainer />
        </div>
    </div>
)

export default World
