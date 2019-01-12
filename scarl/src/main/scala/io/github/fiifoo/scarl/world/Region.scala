package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.world.system.source.StellarBodySourceId

case class Region(id: RegionId,
                  world: WorldId,
                  stellarBody: Option[StellarBodySourceId] = None,
                  entrances: Map[TransportCategory, Set[SiteId]] = Map(),
                  exits: Map[TransportCategory, Set[SiteId]] = Map(),
                  variants: List[RegionVariant] = List(),
                 )
