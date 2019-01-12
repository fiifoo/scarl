package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.world.system.source.SolarSystemSource

case class World(id: WorldId,
                 start: SiteId,
                 characters: List[CreatureKindId],
                 conduits: List[Conduit.Source],
                 transports: Map[TransportId, RegionId] = Map(),
                 system: SolarSystemSource,
                )
