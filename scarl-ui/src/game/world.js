export const getControlledTransport = world => (
    world.transports.find(x => x.hub === world.site)
)

export const getCurrentRegion = world => (
    world.regions.get(world.sites.get(world.site).region)
)

export const getCurrentSpaceship = world => {
    const transport = getCurrentTransport(world)

    return transport && transport.spaceship ? world.system.ships.get(transport.spaceship) : null
}

export const getCurrentStellarBody = world => {
    const region = getCurrentRegion(world)

    if (region.stellarBody) {
        return world.system.bodies.get(region.stellarBody)
    }

    const spaceship = getCurrentSpaceship(world)

    return spaceship ? world.system.bodies.get(spaceship.port) : null
}

export const getCurrentTransport = world => {
    const region = getCurrentRegion(world)

    return world.transports.find(transport => world.sites.get(transport.hub).region === region.id)
}
